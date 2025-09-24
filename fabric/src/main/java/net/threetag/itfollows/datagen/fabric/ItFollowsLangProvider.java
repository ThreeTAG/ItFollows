package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.threetag.itfollows.IFConfig;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.command.ItFollowsCommand;
import net.threetag.itfollows.item.IFItems;
import net.threetag.itfollows.item.IFJukeboxSongs;

import java.util.concurrent.CompletableFuture;

public abstract class ItFollowsLangProvider extends FabricLanguageProvider {

    protected ItFollowsLangProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
    }

    public static class English extends ItFollowsLangProvider {

        protected English(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            builder.add(ItFollowsCommand.ERROR_CURSE_ALREADY_STARTED, "%s already has an active curse");
            builder.add(ItFollowsCommand.START_CURSE_SUCCESS, "%s received the curse");
            builder.add(ItFollowsCommand.ERROR_NO_ACTIVE_CURSE, "%s has no curse");
            builder.add(ItFollowsCommand.STOP_CURSE_SUCCESS, "%s no longer has the curse");
            builder.add(ItFollowsCommand.POSITION, "%s's entity is at %s (%s blocks away)");

            builder.add(IFItems.MUSIC_DISC_SOLSTICE.get(), "Music Disc");
            builder.add(Util.makeDescriptionId("jukebox_song", IFJukeboxSongs.SOLSTICE.location()), "Disasterpeace - Title");

            this.addConfigTitle(builder, "It Follows");
            this.addConfigCategory(builder, IFConfig.CATEGORY_ENTITY, "Entity Settings");
            this.addConfigEntry(builder, "MOVEMENT_SPEED", "Movement Speed");
            this.addConfigEntry(builder, "BLOCK_BREAK_INTERVAL", "Block Break Interval");
        }
    }

    public static class German extends ItFollowsLangProvider {

        protected German(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "de_de", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            builder.add(ItFollowsCommand.ERROR_CURSE_ALREADY_STARTED, "%s hat den Fluch bereits");
            builder.add(ItFollowsCommand.START_CURSE_SUCCESS, "%s hat nun den Fluch");
            builder.add(ItFollowsCommand.ERROR_NO_ACTIVE_CURSE, "%s hat keinen Fluch");
            builder.add(ItFollowsCommand.STOP_CURSE_SUCCESS, "%s hat nun den Fluch nicht mehr");
            builder.add(ItFollowsCommand.POSITION, "%s's Entität ist bei %s (%s Blöcke entfernt)");

            builder.add(IFItems.MUSIC_DISC_SOLSTICE.get(), "Schallplatte");
            builder.add(Util.makeDescriptionId("jukebox_song", IFJukeboxSongs.SOLSTICE.location()), "Disasterpeace - Title");

            this.addConfigTitle(builder, "It Follows");
            this.addConfigCategory(builder, IFConfig.CATEGORY_ENTITY, "Entitätseinstellungen");
            this.addConfigEntry(builder, "MOVEMENT_SPEED", "Bewegungsgeschwindigkeit");
            this.addConfigEntry(builder, "BLOCK_BREAK_INTERVAL", "Blockbruchintervall");
        }
    }

    public static class Saxon extends ItFollowsLangProvider {

        protected Saxon(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "sxu", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            builder.add(ItFollowsCommand.ERROR_CURSE_ALREADY_STARTED, "%s hat den Fluch schoh");
            builder.add(ItFollowsCommand.START_CURSE_SUCCESS, "%s hat nu den Fluch");
            builder.add(ItFollowsCommand.ERROR_NO_ACTIVE_CURSE, "%s hat keenen Fluch");
            builder.add(ItFollowsCommand.STOP_CURSE_SUCCESS, "%s hat nu keenen Fluch mehr");
            builder.add(ItFollowsCommand.POSITION, "%s's Entität is bei %s (%s Blögge endfernd)");

            builder.add(IFItems.MUSIC_DISC_SOLSTICE.get(), "Schallbladde");
            builder.add(Util.makeDescriptionId("jukebox_song", IFJukeboxSongs.SOLSTICE.location()), "Disasterpeace - Title");

            this.addConfigTitle(builder, "It Follows");
            this.addConfigCategory(builder, IFConfig.CATEGORY_ENTITY, "Entitätseinstellungen");
            this.addConfigEntry(builder, "MOVEMENT_SPEED", "Bewejungsjeschwindichgehd");
            this.addConfigEntry(builder, "BLOCK_BREAK_INTERVAL", "Blockbruchintervall");
        }
    }

    public void addConfigTitle(TranslationBuilder builder, String name) {
        builder.add(ItFollows.MOD_ID + ".midnightconfig.title", name);
    }

    public void addConfigCategory(TranslationBuilder builder, String key, String name) {
        builder.add(ItFollows.MOD_ID + ".midnightconfig.category." + key, name);
    }

    public void addConfigEntry(TranslationBuilder builder, String key, String name) {
        builder.add(ItFollows.MOD_ID + ".midnightconfig." + key, name);
    }
}
