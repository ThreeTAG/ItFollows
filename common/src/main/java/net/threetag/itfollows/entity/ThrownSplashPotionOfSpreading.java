package net.threetag.itfollows.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class ThrownSplashPotionOfSpreading extends ThrownSplashPotion {

    public ThrownSplashPotionOfSpreading(EntityType<? extends ThrownSplashPotion> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownSplashPotionOfSpreading(Level level, LivingEntity owner, ItemStack item) {
        super(level, owner, item);
    }

    public ThrownSplashPotionOfSpreading(Level level, double x, double y, double z, ItemStack stack) {
        super(level, x, y, z, stack);
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.getOwner() instanceof ServerPlayer player) {
                if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof ServerPlayer hitPlayer && player != hitPlayer) {
                    CursePlayerHandler.get(hitPlayer).startCurseInfectedBy(player);
                } else if (result instanceof BlockHitResult blockHit) {
                    AABB aABB = this.getBoundingBox().move(blockHit.getLocation().subtract(this.position()));
                    AABB aABB2 = aABB.inflate(4.0, 2.0, 4.0);
                    List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aABB2);

                    float g = ProjectileUtil.computeMargin(this);
                    if (!list.isEmpty()) {

                        for (LivingEntity livingEntity : list) {
                            if (livingEntity.isAffectedByPotions()) {
                                double d = aABB.distanceToSqr(livingEntity.getBoundingBox().inflate(g));
                                if (d < 16.0) {

                                    if (livingEntity instanceof ServerPlayer hitPlayer && player != hitPlayer) {
                                        CursePlayerHandler.get(hitPlayer).startCurseInfectedBy(player);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int i = 2007;
            serverLevel.levelEvent(i, this.blockPosition(), 7561558);
            this.discard();
        }
    }
}
