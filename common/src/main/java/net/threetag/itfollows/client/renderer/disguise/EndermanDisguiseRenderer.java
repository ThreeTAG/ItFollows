package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.threetag.itfollows.ItFollows;

public class EndermanDisguiseRenderer extends DisguiseRenderer<EndermanRenderState> {

    public EndermanDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, EntityType.ENDERMAN);
    }

    @Override
    public ResourceLocation getEyesTexture() {
        return ItFollows.id("textures/disguises/enderman_eyes.png");
    }

    @Override
    public EndermanRenderState createRenderState() {
        return new EndermanRenderState();
    }
}
