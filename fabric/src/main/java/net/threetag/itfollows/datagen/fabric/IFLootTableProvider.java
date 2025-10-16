package net.threetag.itfollows.datagen.fabric;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.threetag.itfollows.block.OminousPotBlockEntity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class IFLootTableProvider extends SimpleFabricLootTableProvider {

    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public IFLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.CHEST);
        this.registryLookup = registryLookup;
    }

    @Override
    public String getName() {
        return "ItFollows Loot Tables";
    }

    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output, HolderLookup.Provider registries) {
        output.accept(OminousPotBlockEntity.DEFAULT_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(new ConstantValue(2))
                                        .add(LootItem.lootTableItem(Items.DIAMOND))
                                        .add(LootItem.lootTableItem(Items.EMERALD))
                                        .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_INGOT))
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(new ConstantValue(1))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_HELMET).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_CHESTPLATE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_LEGGINGS).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_BOOTS).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.DIAMOND_AXE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_PICKAXE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_SWORD).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                                        .add(LootItem.lootTableItem(Items.NETHERITE_AXE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)))
                        ));
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        HashMap<ResourceLocation, LootTable> builders = Maps.newHashMap();
        HashMap<ResourceLocation, ResourceCondition[]> conditionMap = new HashMap<>();

        return registryLookup.thenCompose(lookup -> {
            this.generate((registryKey, builder) -> {
                ResourceCondition[] conditions = FabricDataGenHelper.consumeConditions(builder);
                conditionMap.put(registryKey.location(), conditions);

                if (builders.put(registryKey.location(), builder.setParamSet(contextType).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + registryKey.location());
                }
            }, lookup);

            RegistryOps<JsonElement> ops = lookup.createSerializationContext(JsonOps.INSTANCE);
            final List<CompletableFuture<?>> futures = new ArrayList<>();

            for (Map.Entry<ResourceLocation, LootTable> entry : builders.entrySet()) {
                JsonObject tableJson = (JsonObject) LootTable.DIRECT_CODEC.encodeStart(ops, entry.getValue()).getOrThrow(IllegalStateException::new);
                FabricDataGenHelper.addConditions(tableJson, conditionMap.remove(entry.getKey()));
                futures.add(DataProvider.saveStable(writer, tableJson, getOutputPath(this.output, entry.getKey())));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private static Path getOutputPath(FabricDataOutput dataOutput, ResourceLocation lootTableId) {
        return dataOutput.createRegistryElementsPathProvider(Registries.LOOT_TABLE).json(lootTableId);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

    }
}
