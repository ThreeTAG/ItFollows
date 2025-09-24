package net.threetag.itfollows.datagen.neoforge;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.item.IFJukeboxSongs;

import java.util.concurrent.CompletableFuture;

public class JukeboxSongsProvider extends JsonCodecProvider<JukeboxSong> {

    public JukeboxSongsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, PackOutput.Target.RESOURCE_PACK, "jukebox_song", JukeboxSong.DIRECT_CODEC, lookupProvider, ItFollows.MOD_ID);
    }

    @Override
    protected void gather() {
        IFJukeboxSongs.bootstrap((key, song) -> this.unconditional(key.location(), song));
    }
}
