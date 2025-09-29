package net.threetag.itfollows.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.threetag.itfollows.ItFollows;

public class IFSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ItFollows.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> ENTITY_APPROACHING = SOUND_EVENTS.register("entity_approaching", () -> SoundEvent.createVariableRangeEvent(ItFollows.id("entity_approaching")));
    public static final RegistrySupplier<SoundEvent> MUSIC_DISC_SOLSTICE = SOUND_EVENTS.register("music_disc_solstice", () -> SoundEvent.createVariableRangeEvent(ItFollows.id("music_disc_solstice")));

}
