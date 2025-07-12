package net.threetag.itfollows.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.threetag.itfollows.entity.TheEntity;

public class FollowTargetGoal extends Goal {

    private final TheEntity entity;
    private final PathNavigation navigation;
    private int timeToRecalcPath;

    public FollowTargetGoal(TheEntity entity) {
        this.entity = entity;
        this.navigation = entity.getNavigation();
    }

    @Override
    public boolean canUse() {
        return this.entity.getTargetPlayer() != null;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.navigation.stop();
    }

    @Override
    public void tick() {
        var target = this.entity.getTargetPlayer();

        if (target != null) {
            this.entity.getLookControl().setLookAt(target, 10.0F, this.entity.getMaxHeadXRot());

            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.navigation.moveTo(target, 0.3);
            }
        }
    }

}
