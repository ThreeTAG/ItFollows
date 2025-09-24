package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundSource;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.sound.IFSoundEvents;

import java.util.concurrent.CompletableFuture;

public class ItFollowsSoundProvider extends FabricSoundsProvider {

    public ItFollowsSoundProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(IFSoundEvents.MUSIC_DISC_SOLSTICE.getId(), SoundTypeBuilder.of(IFSoundEvents.MUSIC_DISC_SOLSTICE.get())
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("records/solstice")).stream(true))
                .category(SoundSource.RECORDS));
    }

    @Override
    public String getName() {
        return "ItFollows Sound Definitions";
    }
}
