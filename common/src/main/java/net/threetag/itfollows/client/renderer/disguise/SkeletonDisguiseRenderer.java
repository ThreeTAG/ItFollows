package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

public class SkeletonDisguiseRenderer extends DisguiseRenderer<SkeletonRenderState> {

    public SkeletonDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, EntityType.SKELETON);
    }

    @Override
    public ResourceLocation getEyesTexture() {
        return ItFollows.id("textures/disguises/skeleton_eyes.png");
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
        renderState.entityType = EntityType.SKELETON;
    }
}
