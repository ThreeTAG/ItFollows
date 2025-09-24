package net.threetag.itfollows.item;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.sound.IFSoundEvents;

import java.util.function.BiConsumer;

public class IFJukeboxSongs {

    public static final ResourceKey<JukeboxSong> SOLSTICE = ResourceKey.create(Registries.JUKEBOX_SONG, ItFollows.id("solstice"));

    public static void bootstrap(BiConsumer<ResourceKey<JukeboxSong>, JukeboxSong> registrar) {
        registrar.accept(SOLSTICE, new JukeboxSong(IFSoundEvents.MUSIC_DISC_SOLSTICE, Component.translatable(Util.makeDescriptionId("jukebox_song", SOLSTICE.location())), 131, 6));
    }

}
