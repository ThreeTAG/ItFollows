package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.threetag.itfollows.IFConfig;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.block.IFBlocks;
import net.threetag.itfollows.command.ItFollowsCommand;
import net.threetag.itfollows.entity.IFEntityTypes;
import net.threetag.itfollows.item.IFItems;
import net.threetag.itfollows.item.IFJukeboxSongs;

import java.util.concurrent.CompletableFuture;

public abstract class IFLangProvider extends FabricLanguageProvider {

    protected IFLangProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
    }

    public static class English extends IFLangProvider {

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

            builder.add(IFBlocks.OMINOUS_POT.get(), "Ominous Pot");
            builder.add(IFItems.OMINOUS_POT.get(), "Ominous Pot");
            builder.add(IFItems.MUSIC_DISC_SOLSTICE.get(), "Music Disc");
            builder.add(Util.makeDescriptionId("jukebox_song", IFJukeboxSongs.SOLSTICE.location()), "Disasterpeace - Title");
            builder.add(IFItems.SPLASH_POTION_OF_SPREADING.get(), "Splash Potion of Spreading");

            builder.add(IFEntityTypes.THE_ENTITY.get(), "The Entity");

            this.addConfigTitle(builder, "It Follows");
            this.addConfigCategory(builder, IFConfig.CATEGORY_ENTITY, "Entity Settings");
            this.addConfigEntry(builder, "MOVEMENT_SPEED", "Movement Speed");
            this.addConfigEntry(builder, "BLOCK_BREAK_INTERVAL", "Block Break Interval");

            builder.add(IFAdvancementProvider.ADVANCEMENT_RECEIVE_CURSE_TITLE, "It doesn't think... it doesn't feel...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RECEIVE_CURSE_DESCRIPTION, "It Follows");
            builder.add(IFAdvancementProvider.ADVANCEMENT_BREAK_CURSE_TITLE, "Coda");
            builder.add(IFAdvancementProvider.ADVANCEMENT_BREAK_CURSE_DESCRIPTION, "Do you think it worked?...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RETURN_CURSE_TITLE, "Lingering");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RETURN_CURSE_DESCRIPTION, "It is not done with you...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_PASS_ON_CURSE_TITLE, "Just... pass it along");
            builder.add(IFAdvancementProvider.ADVANCEMENT_PASS_ON_CURSE_DESCRIPTION, "This can't end poorly... right?");
        }
    }

    public static class German extends IFLangProvider {

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

            builder.add(IFBlocks.OMINOUS_POT.get(), "Unheilvoller Krug");
            builder.add(IFItems.OMINOUS_POT.get(), "Unheilvoller Krug");
            builder.add(IFItems.MUSIC_DISC_SOLSTICE.get(), "Schallplatte");
            builder.add(Util.makeDescriptionId("jukebox_song", IFJukeboxSongs.SOLSTICE.location()), "Disasterpeace - Title");
            builder.add(IFItems.SPLASH_POTION_OF_SPREADING.get(), "Wurftrank der Verbreitung");

            builder.add(IFEntityTypes.THE_ENTITY.get(), "Das Wesen");

            this.addConfigTitle(builder, "It Follows");
            this.addConfigCategory(builder, IFConfig.CATEGORY_ENTITY, "Entitätseinstellungen");
            this.addConfigEntry(builder, "MOVEMENT_SPEED", "Bewegungsgeschwindigkeit");
            this.addConfigEntry(builder, "BLOCK_BREAK_INTERVAL", "Blockbruchintervall");

            builder.add(IFAdvancementProvider.ADVANCEMENT_RECEIVE_CURSE_TITLE, "Es denkt nicht... es fühlt nicht...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RECEIVE_CURSE_DESCRIPTION, "Es folgt");
            builder.add(IFAdvancementProvider.ADVANCEMENT_BREAK_CURSE_TITLE, "Coda");
            builder.add(IFAdvancementProvider.ADVANCEMENT_BREAK_CURSE_DESCRIPTION, "Denkst du, es hat funktioniert?...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_PASS_ON_CURSE_TITLE, "Einfach... weitergeben");
            builder.add(IFAdvancementProvider.ADVANCEMENT_PASS_ON_CURSE_DESCRIPTION, "Das kann doch nicht schiefgehen... oder?");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RETURN_CURSE_TITLE, "Verweilend");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RETURN_CURSE_DESCRIPTION, "Es ist noch nicht fertig mit dir...");
        }
    }

    public static class Saxon extends IFLangProvider {

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

            builder.add(IFBlocks.OMINOUS_POT.get(), "Unheilvoller Grug");
            builder.add(IFItems.OMINOUS_POT.get(), "Unheilvoller Grug");
            builder.add(IFItems.MUSIC_DISC_SOLSTICE.get(), "Schallbladde");
            builder.add(Util.makeDescriptionId("jukebox_song", IFJukeboxSongs.SOLSTICE.location()), "Disasterpeace - Title");
            builder.add(IFItems.SPLASH_POTION_OF_SPREADING.get(), "Wurftrank der Verbreitung");

            builder.add(IFEntityTypes.THE_ENTITY.get(), "Das Wesen");

            this.addConfigTitle(builder, "It Follows");
            this.addConfigCategory(builder, IFConfig.CATEGORY_ENTITY, "Entitätseinstellungen");
            this.addConfigEntry(builder, "MOVEMENT_SPEED", "Bewejungsjeschwindichgehd");
            this.addConfigEntry(builder, "BLOCK_BREAK_INTERVAL", "Blockbruchintervall");

            builder.add(IFAdvancementProvider.ADVANCEMENT_RECEIVE_CURSE_TITLE, "Es denkt nisch... es fühlt nisch...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RECEIVE_CURSE_DESCRIPTION, "Es follgt");
            builder.add(IFAdvancementProvider.ADVANCEMENT_BREAK_CURSE_TITLE, "Coda");
            builder.add(IFAdvancementProvider.ADVANCEMENT_BREAK_CURSE_DESCRIPTION, "Denksch, es hot funkti'niert?...");
            builder.add(IFAdvancementProvider.ADVANCEMENT_PASS_ON_CURSE_TITLE, "Einfach... weidageem");
            builder.add(IFAdvancementProvider.ADVANCEMENT_PASS_ON_CURSE_DESCRIPTION, "Des kann doch nisch schiefgeehn... oddor?");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RETURN_CURSE_TITLE, "Verweilnd");
            builder.add(IFAdvancementProvider.ADVANCEMENT_RETURN_CURSE_DESCRIPTION, "Es is noch nisch fäddsch mit dir...");
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
