package net.threetag.itfollows.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.itfollows.advancements.IFCriteriaTriggers;
import net.threetag.itfollows.entity.CursePlayerHandler;
import net.threetag.itfollows.entity.TheEntity;
import net.threetag.itfollows.item.IFItems;
import net.threetag.itfollows.sound.IFSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @SuppressWarnings("ConstantValue")
    @Inject(method = "checkTotemDeathProtection", at = @At("RETURN"))
    private void checkTotemDeathProtectionStopCurse(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.getEntity() instanceof TheEntity entity && (Object) this instanceof ServerPlayer player) {
            if (cir.getReturnValue()) {
                entity.drop(IFItems.MUSIC_DISC_SOLSTICE.get().getDefaultInstance(), true, false);
                IFCriteriaTriggers.BROKE_CURSE.get().trigger(player);
                player.level().setWeatherParameters(0, ServerLevel.THUNDER_DURATION.sample(RandomSource.create()), true, true);
            }

            player.connection.send(new ClientboundSoundEntityPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(IFSoundEvents.KILLED_BY_ENTITY.get()), SoundSource.HOSTILE, player, 1F, 1F, player.getRandom().nextLong()));
            CursePlayerHandler.get(player).stopCurse(!cir.getReturnValue());
        }
    }

}
