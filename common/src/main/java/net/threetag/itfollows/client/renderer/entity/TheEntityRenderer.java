package net.threetag.itfollows.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.client.renderer.disguise.DisguiseRendererRegistry;
import net.threetag.itfollows.entity.TheEntity;
import net.threetag.itfollows.entity.disguise.DisguiseType;

public class TheEntityRenderer extends LivingEntityRenderer<TheEntity, LivingEntityRenderState, TheEntityRenderer.Model> {

    public TheEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TheEntityRenderer.Model(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public void render(LivingEntityRenderState entityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (entityRenderState instanceof TheEntityRenderer.RenderState renderState) {
            var disguise = renderState.disguiseType;
            var renderer = DisguiseRendererRegistry.getRenderer(disguise);
            renderer.render(poseStack, multiBufferSource, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState renderState) {
        return null;
    }

    @Override
    public TheEntityRenderer.RenderState createRenderState() {
        return new TheEntityRenderer.RenderState();
    }

    @Override
    public void extractRenderState(TheEntity livingEntity, LivingEntityRenderState entityRenderState, float f) {
        super.extractRenderState(livingEntity, entityRenderState, f);

        if (entityRenderState instanceof TheEntityRenderer.RenderState renderState) {
            renderState.disguiseType = livingEntity.getDisguiseType();
            var renderer = DisguiseRendererRegistry.getRenderer(renderState.disguiseType);
            renderState.renderState = renderer.reusedState;
            renderer.extractRenderState(livingEntity, renderState.renderState, f);
        }
    }

    @Override
    public boolean shouldRender(TheEntity entity, Frustum frustum, double d, double e, double f) {
        return entity.getDisguiseType() != null && (Minecraft.getInstance().player == null || Minecraft.getInstance().player.isCreative() || Minecraft.getInstance().player.getUUID().equals(entity.getTargetId()) || Minecraft.getInstance().player.getUUID().equals(entity.getTargetInfectorId()));
    }

    public static class RenderState extends PlayerRenderState {

        public DisguiseType disguiseType;
        public LivingEntityRenderState renderState;

    }

    public static class Model extends EntityModel<LivingEntityRenderState> {

        protected Model(ModelPart root) {
            super(root);
        }
    }
}
