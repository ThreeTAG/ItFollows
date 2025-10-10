package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

public class CreeperDisguiseRenderer extends DisguiseRenderer<CreeperRenderState> {

    public CreeperDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, EntityType.CREEPER);
    }

    @Override
    public ResourceLocation getEyesTexture() {
        return ItFollows.id("textures/disguises/creeper_eyes.png");
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
