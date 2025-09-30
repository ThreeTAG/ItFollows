package net.threetag.itfollows.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.threetag.itfollows.IFConfig;
import net.threetag.itfollows.advancements.IFCriteriaTriggers;
import net.threetag.itfollows.sound.IFSoundEvents;

import java.util.Objects;
import java.util.UUID;

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
    private UUID infectedBy = null;

    public CursePlayerHandler(ServerPlayer player) {
        this.player = player;
    }

    public void read(ValueInput input) {
        this.curseActive = input.getBooleanOr("curse_active", false);
        this.lastKnownEntityId = input.getIntOr("last_known_entity_id", -1);
        this.entityPosition = input.read("entity_position", Vec3.CODEC).orElse(null);
        this.infectedBy = input.read("infected_by", UUIDUtil.CODEC).orElse(null);
    }

    public void write(ValueOutput output) {
        output.putBoolean("curse_active", this.curseActive);
        output.putInt("last_known_entity_id", this.lastKnownEntityId);
        output.storeNullable("entity_position", Vec3.CODEC, this.entityPosition);
        output.storeNullable("infected_by", UUIDUtil.CODEC, this.infectedBy);
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

                    if (!this.spawnNewEntity()) {
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

    public boolean startCurseFresh() {
        if (!this.curseActive) {
            this.curseActive = true;
            this.infectedBy = null;
            this.setEntityPosition(DEFAULT_DISTANCE);
            this.spawnNewEntity();
            this.player.connection.send(new ClientboundSoundEntityPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(IFSoundEvents.ENTITY_APPROACHING.get()), SoundSource.HOSTILE, this.player, 1F, 1F, this.player.getRandom().nextLong()));
            IFCriteriaTriggers.RECEIVED_CURSE.get().trigger(this.player);
            return true;
        }

        return false;
    }

    public boolean startCurseAtDistanceFresh(int distance) {
        if (!this.curseActive) {
            this.curseActive = true;
            this.infectedBy = null;
            this.setEntityPosition(distance);
            this.spawnNewEntity();
            this.player.playSound(IFSoundEvents.ENTITY_APPROACHING.get());
            this.player.connection.send(new ClientboundSoundEntityPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(IFSoundEvents.ENTITY_APPROACHING.get()), SoundSource.HOSTILE, this.player, 1F, 1F, this.player.getRandom().nextLong()));
            IFCriteriaTriggers.RECEIVED_CURSE.get().trigger(this.player);
            return true;
        }

        return false;
    }

    public boolean startCurseAtPositionReturned(Vec3 entityPosition) {
        if (!this.curseActive) {
            this.curseActive = true;
            this.entityPosition = entityPosition;
            this.spawnNewEntity();
            this.player.playSound(IFSoundEvents.ENTITY_APPROACHING.get());
            this.player.connection.send(new ClientboundSoundEntityPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(IFSoundEvents.ENTITY_APPROACHING.get()), SoundSource.HOSTILE, this.player, 1F, 1F, this.player.getRandom().nextLong()));
            IFCriteriaTriggers.RECEIVED_CURSE.get().trigger(this.player);
            return true;
        }

        return false;
    }

    public boolean startCurseInfectedBy(ServerPlayer infectedBy) {
        var infectedByHandler = CursePlayerHandler.get(infectedBy);

        if (!this.isCurseActive() && infectedByHandler.isCurseActive()) {
            this.curseActive = true;
            this.entityPosition = infectedByHandler.entityPosition;
            this.spawnNewEntity();
            this.player.connection.send(new ClientboundSoundEntityPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(IFSoundEvents.ENTITY_APPROACHING.get()), SoundSource.HOSTILE, this.player, 1F, 1F, this.player.getRandom().nextLong()));
            IFCriteriaTriggers.RECEIVED_CURSE.get().trigger(this.player);
            IFCriteriaTriggers.PASSED_ON_CURSE.get().trigger(infectedBy);
            infectedByHandler.stopCurse(false);
            return true;
        }

        return false;
    }

    public void stopCurse(boolean revertToInfectedBy) {
        if (this.curseActive) {
            if (revertToInfectedBy && this.infectedBy != null) {
                // TODO handle offline players
                var infect = Objects.requireNonNull(this.player.getServer()).getPlayerList().getPlayer(this.infectedBy);

                if (infect != null && CursePlayerHandler.get(infect).startCurseAtPositionReturned(this.entityPosition)) {
                    IFCriteriaTriggers.RETURNED_CURSE.get().trigger(infect);
                }
            }

            if (this.lastKnownEntity != null) {
                this.lastKnownEntity.discard();
            }
            this.entityPosition = null;
            this.curseActive = false;
            this.infectedBy = null;
        }
    }

    private void setEntityPosition(int distance) {
        this.entityPosition = TheEntity.getRandomPos(this.player.level(), this.player.position(), distance, this.player.getRandom());
    }

    private boolean spawnNewEntity() {
        if (this.curseActive) {
            if (this.lastKnownEntity != null) {
                this.lastKnownEntity.discard();
            }

            if (this.entityPosition == null) {
                this.setEntityPosition(DEFAULT_DISTANCE);
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
                destroyBlock(this.player.level(), blockPosInFront, this.lastKnownEntity);
                destroyBlock(this.player.level(), blockPosInFront.above(), this.lastKnownEntity);

                if (this.player.level().isEmptyBlock(blockPosInFront.below())) {
                    this.player.level().setBlock(blockPosInFront.below(), Blocks.DIRT.defaultBlockState(), 3);
                }
            } else if (y < playerY) {
                if (this.player.getBlockX() == this.lastKnownEntity.getBlockX() && this.player.getBlockZ() == this.lastKnownEntity.getBlockZ()) {
                    this.lastKnownEntity.jumpFromGround();
                    this.player.level().setBlock(this.lastKnownEntity.blockPosition(), Blocks.DIRT.defaultBlockState(), 3);
                    destroyBlock(this.player.level(), this.lastKnownEntity.blockPosition().above(2), this.lastKnownEntity);
                } else {
                    destroyBlock(this.player.level(), blockPosInFront.above(), this.lastKnownEntity);
                    destroyBlock(this.player.level(), blockPosInFront.above(2), this.lastKnownEntity);
                    destroyBlock(this.player.level(), blockPosInFront.above(3), this.lastKnownEntity);

                    if (this.player.level().isEmptyBlock(blockPosInFront)) {
                        this.player.level().setBlock(blockPosInFront, Blocks.DIRT.defaultBlockState(), 3);
                    }
                }
            } else {
                if (this.player.getBlockX() == this.lastKnownEntity.getBlockX() && this.player.getBlockZ() == this.lastKnownEntity.getBlockZ()) {
                    destroyBlock(this.player.level(), this.lastKnownEntity.blockPosition().below(), this.lastKnownEntity);
                } else {
                    destroyBlock(this.player.level(), blockPosInFront.above(), this.lastKnownEntity);
                    destroyBlock(this.player.level(), blockPosInFront, this.lastKnownEntity);
                    destroyBlock(this.player.level(), blockPosInFront.below(), this.lastKnownEntity);
                }

                if (this.player.level().isEmptyBlock(blockPosInFront.below(2))) {
                    this.player.level().setBlock(blockPosInFront.below(2), Blocks.DIRT.defaultBlockState(), 3);
                }
            }
        }
    }

    private void destroyBlock(Level level, BlockPos pos, Entity entity) {
        if (level.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
            level.destroyBlock(pos, false, entity);
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

    public double getEntityDistance(Vec3 pos) {
        return this.entityPosition != null ? this.entityPosition.distanceTo(pos) : -1;
    }

    public UUID getInfectedBy() {
        return this.infectedBy;
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
