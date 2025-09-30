package net.threetag.itfollows.entity.ai.goal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.threetag.itfollows.entity.CursePlayerHandler;
import net.threetag.itfollows.entity.TheEntity;

public class KillTargetGoal extends Goal {

    private final TheEntity entity;

    public KillTargetGoal(TheEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        var target = this.entity.getTargetPlayer();
        return target != null && target.distanceTo(this.entity) <= 2;
    }

    @Override
    public void start() {
        var target = this.entity.getTargetPlayer();

        if (target != null && target.isAlive() && this.entity.isAlive() && target instanceof ServerPlayer player && !player.isCreative()) {
            target.hurtServer(player.level(), player.level().damageSources().mobAttack(this.entity), Float.MAX_VALUE);
            CursePlayerHandler.get(player).stopCurse(true);
        }
    }
}
