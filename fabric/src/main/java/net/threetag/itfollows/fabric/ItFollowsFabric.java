package net.threetag.itfollows.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.item.IFItems;

public final class ItFollowsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ItFollows.init();

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerItemRecipe(Items.SPLASH_POTION, Ingredient.of(Items.ECHO_SHARD), IFItems.SPLASH_POTION_OF_SPREADING.get());
        });
    }
}
