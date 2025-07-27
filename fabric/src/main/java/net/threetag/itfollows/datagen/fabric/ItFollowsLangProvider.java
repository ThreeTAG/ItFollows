package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.threetag.itfollows.command.ItFollowsCommand;

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
        }
    }
}
