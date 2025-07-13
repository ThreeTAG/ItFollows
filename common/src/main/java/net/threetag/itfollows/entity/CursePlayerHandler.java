package net.threetag.itfollows.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;

public class CursePlayerHandler {

    private final ServerPlayer player;
    private boolean curseActive = false;
    private TheEntity lastKnownEntity;
    private int lastKnownEntityId = -1;
    private BlockPos lastKnownPosition = null;

    public CursePlayerHandler(ServerPlayer player) {
        this.player = player;
    }

    public void read(CompoundTag nbt) {
        this.curseActive = nbt.getBooleanOr("curse_active", false);
        this.lastKnownEntityId = nbt.getIntOr("last_known_entity_id", -1);
        this.lastKnownPosition = nbt.contains("last_known_position") ? BlockPos.CODEC.parse(NbtOps.INSTANCE, nbt.get("last_known_position")).getOrThrow() : null;
    }

    public CompoundTag write() {
        var nbt = new CompoundTag();
        nbt.putBoolean("curse_active", this.curseActive);
        nbt.putInt("last_known_entity_id", this.lastKnownEntityId);
        if (this.lastKnownPosition != null) {
            nbt.put("last_known_position", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, this.lastKnownPosition).getOrThrow());
        }
        return nbt;
    }

    public void tick() {
        if (this.curseActive) {
            if (this.lastKnownEntity == null || this.lastKnownEntity.isRemoved()) {
                this.spawnNewEntity();
            }
        }
    }

    public void startCurse() {
        if (!this.curseActive) {
            this.curseActive = true;
            this.lastKnownPosition = null;
            this.spawnNewEntity();
        }
    }

    public void stopCurse() {
        if (this.curseActive) {
            if (this.lastKnownEntity != null) {
                this.lastKnownEntity.discard();
            }
            this.curseActive = false;
        }
    }

    private void spawnNewEntity() {
        if (this.curseActive) {
            if (this.lastKnownEntity != null) {
                this.lastKnownEntity.discard();
            }

            this.lastKnownEntity = new TheEntity(this.player, 50);

            if (this.lastKnownPosition != null) {
                this.lastKnownEntity.setPos(this.lastKnownPosition.getCenter());
            }

            this.player.level().addFreshEntity(this.lastKnownEntity);
            this.lastKnownEntityId = this.lastKnownEntity.getId();
            this.lastKnownPosition = this.lastKnownEntity.blockPosition();
        }
    }

    public boolean setLastKnownEntity(TheEntity entity) {
        if (this.lastKnownEntityId == entity.getId()) {
            this.lastKnownEntity = entity;
            this.lastKnownEntityId = entity.getId();
            this.lastKnownPosition = entity.blockPosition();
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
