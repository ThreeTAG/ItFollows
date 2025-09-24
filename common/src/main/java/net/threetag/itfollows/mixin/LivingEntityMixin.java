package net.threetag.itfollows.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.itfollows.entity.TheEntity;
import net.threetag.itfollows.item.IFItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private void checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("HALLO 1");
        if (damageSource.getEntity() instanceof TheEntity entity) {
            System.out.println("HALLO 2");
            var item = entity.drop(IFItems.MUSIC_DISC_SOLSTICE.get().getDefaultInstance(), true, false);
            System.out.println("HALLO 3 " + item.position().toString());
        }
    }

}
