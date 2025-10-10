package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.item.ItemStack;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

public class PigDisguiseRenderer extends DisguiseRenderer<PigRenderState> {

    public PigDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, EntityType.PIG);
    }

    @Override
    public ResourceLocation getEyesTexture() {
        return ItFollows.id("textures/disguises/pig_eyes.png");
    }

    @Override
    public PigRenderState createRenderState() {
        return new PigRenderState();
    }

    @Override
    public void extractRenderState(TheEntity entity, PigRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.saddle = ItemStack.EMPTY;
        renderState.variant = entity.registryAccess().getOrThrow(PigVariants.DEFAULT).value();
    }
}
