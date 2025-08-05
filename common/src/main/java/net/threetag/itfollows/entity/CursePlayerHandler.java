package net.threetag.itfollows.entity;

import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class CursePlayerHandler {

    public static final int DEFAULT_DISTANCE = 2000;

    private final ServerPlayer player;
    private boolean curseActive = false;
    private TheEntity lastKnownEntity;
    private int lastKnownEntityId = -1;
    private Vec3 entityPosition = null;
    private int updateTimer = 0;

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

            if (this.updateTimer >= 20) {
                if (this.lastKnownEntity == null) {
                    this.lastKnownEntityId = -1;

                    if (!this.spawnNewEntity(DEFAULT_DISTANCE)) {
                        this.simulateEntityMovement();
                    }
                }

                this.updateTimer = 0;
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

            if (this.isEntityPosLoaded()) {
                this.lastKnownEntity = new TheEntity(this.player);
                this.lastKnownEntity.setPos(this.entityPosition);
                boolean spawned = this.player.level().addWithUUID(this.lastKnownEntity);

                if (spawned) {
                    this.lastKnownEntityId = this.lastKnownEntity.getId();
                    placeTicket(this.player.level(), new ChunkPos(
                            SectionPos.blockToSectionCoord(this.entityPosition.x()),
                            SectionPos.blockToSectionCoord(this.entityPosition.z())
                    ));
                }

                return spawned;
            }
        }

        return false;
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

    public long registerAndUpdateTicket(TheEntity theEntity) {
        if (theEntity.level() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = theEntity.chunkPosition();
            this.setLastKnownEntity(theEntity);
            serverLevel.resetEmptyTime();
            return placeTicket(serverLevel, chunkPos) - 1L;
        } else {
            return 0L;
        }
    }

    public static long placeTicket(ServerLevel serverLevel, ChunkPos chunkPos) {
        serverLevel.getChunkSource().addTicketWithRadius(IFTicketTypes.THE_ENTITY.get(), chunkPos, 2);
        return TicketType.ENDER_PEARL.timeout();
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
