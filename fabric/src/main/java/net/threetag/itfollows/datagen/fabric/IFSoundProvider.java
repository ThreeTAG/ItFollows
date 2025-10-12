package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundSource;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.sound.IFSoundEvents;

import java.util.concurrent.CompletableFuture;

public class IFSoundProvider extends FabricSoundsProvider {

    public IFSoundProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(IFSoundEvents.ENTITY_APPROACHING.getId(), SoundTypeBuilder.of(IFSoundEvents.ENTITY_APPROACHING.get())
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_1")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_2")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_3")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_4")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_5")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_6")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_7")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/approaching_8")))
                .category(SoundSource.HOSTILE));
        exporter.add(IFSoundEvents.ENTITY_CHARGING.getId(), SoundTypeBuilder.of(IFSoundEvents.ENTITY_CHARGING.get())
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/charging")))
                .category(SoundSource.HOSTILE));
        exporter.add(IFSoundEvents.KILLED_BY_ENTITY.getId(), SoundTypeBuilder.of(IFSoundEvents.KILLED_BY_ENTITY.get())
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/killed_by_1")))
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("entity/killed_by_2")))
                .category(SoundSource.HOSTILE));
        exporter.add(IFSoundEvents.MUSIC_DISC_SOLSTICE.getId(), SoundTypeBuilder.of(IFSoundEvents.MUSIC_DISC_SOLSTICE.get())
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(ItFollows.id("records/solstice")).stream(true))
                .category(SoundSource.RECORDS));
    }

    @Override
    public String getName() {
        return "ItFollows Sound Definitions";
    }
}
