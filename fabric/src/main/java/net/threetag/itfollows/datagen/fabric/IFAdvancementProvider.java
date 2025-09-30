package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.advancements.IFCriteriaTriggers;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class IFAdvancementProvider extends FabricAdvancementProvider {

    public static final String ADVANCEMENT_RECEIVE_CURSE_TITLE = Util.makeDescriptionId("advancements", ItFollows.id("receive_curse.title"));
    public static final String ADVANCEMENT_RECEIVE_CURSE_DESCRIPTION = Util.makeDescriptionId("advancements", ItFollows.id("receive_curse.description"));

    protected IFAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        AdvancementHolder receiveCurse = Advancement.Builder.advancement()
                .display(
                        Items.OMINOUS_BOTTLE,
                        Component.translatable(ADVANCEMENT_RECEIVE_CURSE_TITLE),
                        Component.translatable(ADVANCEMENT_RECEIVE_CURSE_DESCRIPTION),
                        null,
                        AdvancementType.TASK,
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                .parent(Advancement.Builder.advancement().build(ResourceLocation.withDefaultNamespace("adventure/kill_mob_near_sculk_catalyst")))
                .addCriterion("received_curse", IFCriteriaTriggers.receivedCurse(EntityPredicate.Builder.entity()))
                .build(ItFollows.id("receive_curse"));
        consumer.accept(receiveCurse);
    }
}
