package net.threetag.itfollows.entity;

import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.threetag.itfollows.IFConfig;

public class CursePlayerHandler {

    public static final int DEFAULT_DISTANCE = 2000;
    public static final int DESPAWN_DISTANCE = 100;

    private final ServerPlayer player;
    private boolean curseActive = false;
    private TheEntity lastKnownEntity;
    private int lastKnownEntityId = -1;
    private Vec3 entityPosition = null;
    private int updateTimer = 0;
    private int isStuckTimer = 0;
    private Vec3 isStuckPositionTracker = null;

    public CursePlayerHandler(ServerPlayer player) {
        this.player = player;
    }

    public void read(ValueInput input) {
        this.curseActive = input.getBooleanOr("curse_active", false);
        this.lastKnownEntityId = input.getIntOr("last_known_entity_id", -1);
        this.entityPosition = input.read("entity_position", Vec3.CODEC).orElse(null);
    }

    public void write(ValueOutput output) {
        output.putBoolean("curse_active", this.curseActive);
        output.putInt("last_known_entity_id", this.lastKnownEntityId);
        output.storeNullable("entity_position", Vec3.CODEC, this.entityPosition);
    }

    public void tick() {
        if (this.curseActive) {
            this.updateTimer++;
            this.isStuckTimer++;

            if (this.updateTimer >= 20) {
                if (this.lastKnownEntity != null && (this.lastKnownEntity.isRemoved() || this.lastKnownEntity.position().distanceTo(this.player.position()) > DESPAWN_DISTANCE)) {
                    this.removeEntity(this.lastKnownEntity);
                }

                if (this.lastKnownEntity == null) {
                    this.lastKnownEntityId = -1;

                    if (!this.spawnNewEntity(DEFAULT_DISTANCE)) {
                        this.simulateEntityMovement();
                    }
                }

                this.updateTimer = 0;
            }

            if (this.isStuckPositionTracker == null && this.lastKnownEntity != null) {
                this.isStuckPositionTracker = this.lastKnownEntity.position();
            }

            if (this.isStuckTimer >= 20 * IFConfig.BLOCK_BREAK_INTERVAL && this.lastKnownEntity != null) {
                var blocksTravelled = this.isStuckPositionTracker.distanceTo(this.lastKnownEntity.position());
                this.isStuckPositionTracker = this.lastKnownEntity.position();
                this.isStuckTimer = 0;

                if (blocksTravelled < IFConfig.BLOCK_BREAK_INTERVAL / 2D) {
                    this.breakBlocksInPath();
                }
            }
        }
    }

    public void startCurse(int distance) {
        if (!this.curseActive) {
            this.curseActive = true;
            this.entityPosition = null;
            this.spawnNewEntity(distance);
        }
    }

    public void stopCurse() {
        if (this.curseActive) {
            if (this.lastKnownEntity != null) {
                this.lastKnownEntity.discard();
            }
            this.entityPosition = null;
            this.curseActive = false;
        }
    }

    private boolean spawnNewEntity(int distance) {
        if (this.curseActive) {
            if (this.lastKnownEntity != null) {
                this.lastKnownEntity.discard();
            }

            if (this.entityPosition == null) {
                this.entityPosition = TheEntity.getRandomPos(this.player.level(), this.player.position(), distance, this.player.getRandom());
            }

            if (this.entityPosition.distanceTo(this.player.position()) < DESPAWN_DISTANCE && this.isEntityPosLoaded()) {
                this.lastKnownEntity = new TheEntity(this.player);
                this.lastKnownEntity.setPos(this.entityPosition);
                boolean spawned = this.player.level().addWithUUID(this.lastKnownEntity);

                if (spawned) {
                    this.lastKnownEntityId = this.lastKnownEntity.getId();
                }

                return spawned;
            }
        }

        return false;
    }

    private void breakBlocksInPath() {
        if (this.lastKnownEntity != null) {
            var y = this.lastKnownEntity.getBlockY();
            var playerY = this.player.getBlockY();
            var diffVec = this.player.position().subtract(this.lastKnownEntity.position()).normalize();
            var offset = new Vec3i(diffVec.x < -0.1 ? -1 : diffVec.x > 0.1 ? 1 : 0, 0, diffVec.z < -0.1 ? -1 : diffVec.z > 0.1 ? 1 : 0);

            if (Math.abs(offset.getX()) == 1 && Math.abs(offset.getZ()) == 1) {
                offset = new Vec3i(offset.getX(), 0, 0);
            }

            var blockPosInFront = this.lastKnownEntity.blockPosition().offset(offset);

            if (y == playerY) {
                this.player.level().destroyBlock(blockPosInFront, false, this.lastKnownEntity);
                this.player.level().destroyBlock(blockPosInFront.above(), false, this.lastKnownEntity);

                if (this.player.level().isEmptyBlock(blockPosInFront.below())) {
                    this.player.level().setBlock(blockPosInFront.below(), Blocks.DIRT.defaultBlockState(), 3);
                }
            } else if (y < playerY) {
                if (this.player.getBlockX() == this.lastKnownEntity.getBlockX() && this.player.getBlockZ() == this.lastKnownEntity.getBlockZ()) {
                    this.lastKnownEntity.jumpFromGround();
                    this.player.level().setBlock(this.lastKnownEntity.blockPosition(), Blocks.DIRT.defaultBlockState(), 3);
                    this.player.level().destroyBlock(this.lastKnownEntity.blockPosition().above(2), false, this.lastKnownEntity);
                } else {
                    this.player.level().destroyBlock(blockPosInFront.above(), false, this.lastKnownEntity);
                    this.player.level().destroyBlock(blockPosInFront.above(2), false, this.lastKnownEntity);
                    this.player.level().destroyBlock(blockPosInFront.above(3), false, this.lastKnownEntity);

                    if (this.player.level().isEmptyBlock(blockPosInFront)) {
                        this.player.level().setBlock(blockPosInFront, Blocks.DIRT.defaultBlockState(), 3);
                    }
                }
            } else {
                if (this.player.getBlockX() == this.lastKnownEntity.getBlockX() && this.player.getBlockZ() == this.lastKnownEntity.getBlockZ()) {
                    this.player.level().destroyBlock(this.lastKnownEntity.blockPosition().below(), false, this.lastKnownEntity);
                } else {
                    this.player.level().destroyBlock(blockPosInFront.above(), false, this.lastKnownEntity);
                    this.player.level().destroyBlock(blockPosInFront, false, this.lastKnownEntity);
                    this.player.level().destroyBlock(blockPosInFront.below(), false, this.lastKnownEntity);
                }

                if (this.player.level().isEmptyBlock(blockPosInFront.below(2))) {
                    this.player.level().setBlock(blockPosInFront.below(2), Blocks.DIRT.defaultBlockState(), 3);
                }
            }
        }
    }

    public boolean isEntityPosLoaded() {
        return this.player.level().canSpawnEntitiesInChunk(new ChunkPos(
                SectionPos.blockToSectionCoord(this.entityPosition.x()),
                SectionPos.blockToSectionCoord(this.entityPosition.z())
        ));
    }

    public void simulateEntityMovement() {
        if (this.lastKnownEntity == null) {
            this.entityPosition = this.entityPosition.add(this.player.position().subtract(this.entityPosition).normalize());
        }
    }

    public boolean setLastKnownEntity(TheEntity entity) {
        if (this.lastKnownEntityId == entity.getId()) {
            this.lastKnownEntity = entity;
            this.lastKnownEntityId = entity.getId();
            this.entityPosition = entity.position();
            return true;
        } else {
            return false;
        }
    }

    public void removeEntity(TheEntity entity) {
        if (entity.getId() == this.lastKnownEntityId) {
            this.lastKnownEntity = null;
            this.lastKnownEntityId = -1;
        }
    }

    public boolean isCurseActive() {
        return this.curseActive;
    }

    public Vec3 getEntityPosition() {
        return this.entityPosition;
    }

    public static CursePlayerHandler get(ServerPlayer player) {
        if (player instanceof Curseable curseable) {
            return curseable.it_follows$getCurseHandler();
        }
        return null;
    }

    public interface Curseable {

        CursePlayerHandler it_follows$getCurseHandler();

    }
}
