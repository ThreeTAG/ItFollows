package net.threetag.itfollows.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Pose;
import net.threetag.itfollows.client.renderer.disguise.DisguiseRendererRegistry;
import net.threetag.itfollows.entity.TheEntity;
import net.threetag.itfollows.entity.disguise.DisguiseType;
import org.jetbrains.annotations.NotNull;

public class TheEntityRenderer extends LivingEntityRenderer<TheEntity, TheEntityRenderer.RenderState, PlayerModel> {

    private ResourceLocation cachedTexture;

    public TheEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        DisguiseRendererRegistry.reload(context);
    }

    @Override
    public void render(TheEntityRenderer.RenderState livingEntityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        var disguise = livingEntityRenderState.disguiseType;
        var renderer = DisguiseRendererRegistry.getRenderer(disguise);
        this.cachedTexture = renderer.getTexture();
        var model = renderer.getModel();
        var state = livingEntityRenderState.renderState;

        poseStack.pushPose();
        if (state.hasPose(Pose.SLEEPING)) {
            Direction direction = state.bedOrientation;
            if (direction != null) {
                float f = state.eyeHeight - 0.1F;
                poseStack.translate(-direction.getStepX() * f, 0.0F, -direction.getStepZ() * f);
            }
        }

        float g = state.scale;
        poseStack.scale(g, g, g);
        this.setupRotations(livingEntityRenderState, poseStack, state.bodyRot, g);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(livingEntityRenderState, poseStack);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        model.setupAnim(state);
        boolean bl = this.isBodyVisible(livingEntityRenderState);
        boolean bl2 = !bl && !state.isInvisibleToPlayer;
        RenderType renderType = this.getRenderType(livingEntityRenderState, bl, bl2, state.appearsGlowing);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            int j = getOverlayCoords(state, this.getWhiteOverlayProgress(livingEntityRenderState));
            int k = bl2 ? 654311423 : -1;
            int l = ARGB.multiply(k, this.getModelTint(livingEntityRenderState));
            model.renderToBuffer(poseStack, vertexConsumer, i, j, l);
        }

        poseStack.popPose();
    }

    @Override
    public TheEntityRenderer.RenderState createRenderState() {
        return new TheEntityRenderer.RenderState();
    }

    @Override
    public void extractRenderState(TheEntity livingEntity, TheEntityRenderer.RenderState livingEntityRenderState, float f) {
        super.extractRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.disguiseType = livingEntity.getDisguiseType();
        var renderer = DisguiseRendererRegistry.getRenderer(livingEntityRenderState.disguiseType);
        livingEntityRenderState.renderState = renderer.reusedState;
        renderer.extractRenderState(livingEntity, livingEntityRenderState.renderState, f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(TheEntityRenderer.RenderState livingEntityRenderState) {
        return this.cachedTexture;
    }

    @Override
    public @NotNull PlayerModel getModel() {
        return super.getModel();
    }

    @Override
    public boolean shouldRender(TheEntity entity, Frustum frustum, double d, double e, double f) {
        return Minecraft.getInstance().player == null || Minecraft.getInstance().player.isCreative() || Minecraft.getInstance().player == entity.getTargetPlayer();
    }

    public static class RenderState extends PlayerRenderState {

        public DisguiseType disguiseType;
        public LivingEntityRenderState renderState;

    }
}
