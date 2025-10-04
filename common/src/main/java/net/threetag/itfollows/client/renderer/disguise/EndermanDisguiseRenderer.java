package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.resources.ResourceLocation;

public class EndermanDisguiseRenderer extends DisguiseRenderer<EndermanRenderState, EndermanModel<EndermanRenderState>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/enderman/enderman.png");

    private final EndermanModel<EndermanRenderState> model;

    public EndermanDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EndermanModel<>(context.bakeLayer(ModelLayers.ENDERMAN));
    }

    @Override
    public EndermanModel<EndermanRenderState> getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public EndermanRenderState createRenderState() {
        return new EndermanRenderState();
    }
}
