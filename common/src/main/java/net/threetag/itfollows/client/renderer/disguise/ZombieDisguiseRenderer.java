package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

public class ZombieDisguiseRenderer extends DisguiseRenderer<ZombieRenderState> {

    public ZombieDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, EntityType.ZOMBIE);
    }

    @Override
    public ResourceLocation getEyesTexture() {
        return ItFollows.id("textures/disguises/zombie_eyes.png");
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
