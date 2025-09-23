package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.entity.TheEntity;

public class SkeletonDisguiseRenderer extends DisguiseRenderer<SkeletonRenderState, SkeletonModel<SkeletonRenderState>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    private final SkeletonModel<SkeletonRenderState> model;

    public SkeletonDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON));
    }

    @Override
    public SkeletonModel<SkeletonRenderState> getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }

    @Override
    public void extractRenderState(TheEntity entity, SkeletonRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, renderState, partialTick, this.itemModelResolver);
        renderState.isAggressive = false;
        renderState.isShaking = false;
        renderState.isHoldingBow = false;
    }
}
