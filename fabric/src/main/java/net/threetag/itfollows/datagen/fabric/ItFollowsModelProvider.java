package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.threetag.itfollows.item.IFItems;

public class ItFollowsModelProvider extends FabricModelProvider {

    public ItFollowsModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(IFItems.MUSIC_DISC_SOLSTICE.get(), ModelTemplates.FLAT_ITEM);
    }
}
