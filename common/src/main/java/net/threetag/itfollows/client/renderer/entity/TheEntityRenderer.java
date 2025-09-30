package net.threetag.itfollows.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
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
import net.minecraft.world.item.DebugStickItem;
import net.threetag.itfollows.client.renderer.disguise.DisguiseRendererRegistry;
import net.threetag.itfollows.entity.TheEntity;
import net.threetag.itfollows.entity.disguise.DisguiseType;
import org.jetbrains.annotations.NotNull;

public class TheEntityRenderer extends LivingEntityRenderer<TheEntity, LivingEntityRenderState, TheEntityRenderer.Model> {

    private ResourceLocation cachedTexture;

    public TheEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TheEntityRenderer.Model(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        DisguiseRendererRegistry.reload(context);
    }

    @Override
    public void render(LivingEntityRenderState entityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (entityRenderState instanceof TheEntityRenderer.RenderState renderState) {
            var disguise = renderState.disguiseType;
            var renderer = DisguiseRendererRegistry.getRenderer(disguise);
            this.cachedTexture = renderer.getTexture();
            var model = renderer.getModel();
            var state = renderState.renderState;

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
            renderer.setupRotations(state, poseStack, state.bodyRot, g);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(state, poseStack);
            poseStack.translate(0.0F, -1.501F, 0.0F);
            model.setupAnim(state);
            boolean bl = this.isBodyVisible(state);
            boolean bl2 = !bl && !state.isInvisibleToPlayer;
            boolean highlight = Minecraft.getInstance().player.getMainHandItem().getItem() instanceof DebugStickItem;
            RenderType renderType = this.getRenderType(state, !highlight && bl, !highlight && bl2, highlight || state.appearsGlowing);
            if (renderType != null) {
                VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
                int j = getOverlayCoords(state, this.getWhiteOverlayProgress(state));
                int k = bl2 ? 654311423 : -1;
                int l = ARGB.multiply(k, this.getModelTint(state));
                model.renderToBuffer(poseStack, vertexConsumer, i, j, l);
            }

            poseStack.popPose();
        }
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
    public @NotNull ResourceLocation getTextureLocation(LivingEntityRenderState livingEntityRenderState) {
        return this.cachedTexture;
    }

    @Override
    public boolean shouldRender(TheEntity entity, Frustum frustum, double d, double e, double f) {
        return entity.getDisguiseType() != null && (Minecraft.getInstance().player == null || Minecraft.getInstance().player.isCreative() || Minecraft.getInstance().player.getUUID().equals(entity.getTargetId())|| Minecraft.getInstance().player.getUUID().equals(entity.getTargetInfectorId()));
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
