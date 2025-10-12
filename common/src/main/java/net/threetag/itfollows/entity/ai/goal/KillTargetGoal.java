package net.threetag.itfollows.entity.ai.goal;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.threetag.itfollows.entity.TheEntity;

public class KillTargetGoal extends Goal {

    private final TheEntity entity;

    public KillTargetGoal(TheEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        var target = this.entity.getTargetPlayer();
        return target != null && this.entity.getChargingTimer() >= 0;
    }

    @Override
    public void tick() {
        var target = this.entity.getTargetPlayer();

        if (target instanceof ServerPlayer player && target.isAlive() && this.entity.isAlive() && !player.isCreative()) {
            target.setDeltaMovement(new Vec3(0, 0.05, 0));
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
            this.entity.chargingTimer++;

            if (this.entity.getChargingTimer() >= 20) {
                target.hurtServer(player.level(), player.level().damageSources().mobAttack(this.entity), Float.MAX_VALUE);
                this.entity.discard();
            }
        }
    }
}
