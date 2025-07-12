package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.entity.TheEntity;

public class ZombieDisguiseRenderer extends DisguiseRenderer<ZombieRenderState, ZombieModel<ZombieRenderState>> {

    private static final ResourceLocation ZOMBIE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");

    private final ZombieModel<ZombieRenderState> model;

    public ZombieDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE));
    }

    @Override
    public ZombieModel<ZombieRenderState> getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTexture() {
        return ZOMBIE_LOCATION;
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
}
