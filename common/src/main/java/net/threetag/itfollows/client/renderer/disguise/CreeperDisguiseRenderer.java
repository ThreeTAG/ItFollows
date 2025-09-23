package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.entity.TheEntity;

public class CreeperDisguiseRenderer extends DisguiseRenderer<CreeperRenderState, CreeperModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper.png");

    private final CreeperModel model;

    public CreeperDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CreeperModel(context.bakeLayer(ModelLayers.CREEPER));
    }

    @Override
    public CreeperModel getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public CreeperRenderState createRenderState() {
        return new CreeperRenderState();
    }

    @Override
    public void extractRenderState(TheEntity entity, CreeperRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.swelling = 0F;
        renderState.isPowered = false;
    }
}
