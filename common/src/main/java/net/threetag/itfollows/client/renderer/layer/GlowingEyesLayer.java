package net.threetag.itfollows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GlowingEyesLayer<S extends EntityRenderState, M extends EntityModel<S>> extends EyesLayer<S, M> {

    private final RenderType renderType;
    public boolean enabled = false;

    public GlowingEyesLayer(RenderLayerParent<S, M> renderer, ResourceLocation texture) {
        super(renderer);
        this.renderType = RenderType.eyes(texture);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
        if (enabled) {
            super.render(poseStack, bufferSource, packedLight, renderState, yRot, xRot);
        }
    }

    @Override
    public @NotNull RenderType renderType() {
        return renderType;
    }
}
