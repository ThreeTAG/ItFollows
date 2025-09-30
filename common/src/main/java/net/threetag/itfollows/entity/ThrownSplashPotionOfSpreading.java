package net.threetag.itfollows.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

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
    public void onHitAsPotion(ServerLevel level, ItemStack stack, HitResult hitResult) {
        super.onHitAsPotion(level, stack, hitResult);

        if (this.getOwner() instanceof ServerPlayer player && hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof ServerPlayer hitPlayer && player != hitPlayer) {
            var victimCurse = CursePlayerHandler.get(hitPlayer);
            victimCurse.startCurseInfectedBy(player);
        }
    }
}
