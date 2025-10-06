package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.threetag.itfollows.block.OminousPotBlockEntity;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class IFLootTableProvider extends SimpleFabricLootTableProvider {

    public IFLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.CHEST);
    }

    @Override
    public String getName() {
        return "ItFollows Loot Tables";
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(OminousPotBlockEntity.DEFAULT_LOOT_TABLE, LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.DIAMOND))
                        .add(LootItem.lootTableItem(Items.EMERALD))
                        .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING))
                        .add(LootItem.lootTableItem(Items.NETHER_STAR))
                        .add(LootItem.lootTableItem(Items.NETHERITE_INGOT))
        ));
    }
}
