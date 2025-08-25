package net.threetag.itfollows.client.renderer.disguise;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.itfollows.entity.TheEntity;

public class DrownedDisguiseRenderer extends DisguiseRenderer<ZombieRenderState, DrownedModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png");

    private final DrownedModel model;

    public DrownedDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DrownedModel(context.bakeLayer(ModelLayers.DROWNED));
    }

    @Override
    public DrownedModel getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public void extractRenderState(TheEntity entity, ZombieRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, renderState, partialTick, this.itemModelResolver);
        renderState.isAggressive = false;
        renderState.isConverting = false;
    }

    @Override
    public void setupRotations(ZombieRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {
        super.setupRotations(renderState, poseStack, bodyRot, scale);
        float h = renderState.swimAmount;
        if (h > 0.0F) {
            float i = -10.0F - renderState.xRot;
            float j = Mth.lerp(h, 0.0F, i);
            poseStack.rotateAround(Axis.XP.rotationDegrees(j), 0.0F, renderState.boundingBoxHeight / 2.0F / scale, 0.0F);
        }
    }
}
