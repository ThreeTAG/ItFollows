package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import net.threetag.itfollows.item.IFJukeboxSongs;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class IFJukeboxSongsProvider extends FabricCodecDataProvider<JukeboxSong> {

    public IFJukeboxSongsProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, JukeboxSong> provider, HolderLookup.Provider lookup) {
        IFJukeboxSongs.bootstrap((key, song) -> provider.accept(key.location(), song));
    }

    @Override
    public String getName() {
        return "It Follows Jukebox Songs";
    }
}
