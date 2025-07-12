package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.item.ItemStack;
import net.threetag.itfollows.entity.TheEntity;

public class PigDisguiseRenderer extends DisguiseRenderer<PigRenderState, PigModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/pig/temperate_pig.png");

    private final PigModel model;

    public PigDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PigModel(context.bakeLayer(ModelLayers.PIG));
    }

    @Override
    public PigModel getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public PigRenderState createRenderState() {
        return new PigRenderState();
    }

    @Override
    public void extractRenderState(TheEntity entity, PigRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.saddle = ItemStack.EMPTY;
        renderState.variant = entity.registryAccess().getOrThrow(PigVariants.DEFAULT).value();
    }
}
