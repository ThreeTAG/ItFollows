package net.threetag.itfollows;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.itfollows.entity.CursePlayerHandler;
import net.threetag.itfollows.entity.TheEntity;

public class IFEventHandler implements EntityEvent.LivingDeath {

    public static void init() {
        var instance = new IFEventHandler();
        EntityEvent.LIVING_DEATH.register(instance);
    }

    @Override
    public EventResult die(LivingEntity livingEntity, DamageSource damageSource) {
        if (damageSource.getDirectEntity() instanceof TheEntity && livingEntity instanceof ServerPlayer player) {
            CursePlayerHandler.get(player).stopCurse();
        }

        return EventResult.pass();
    }
}
