package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

public class SheepDisguiseRenderer extends DisguiseRenderer<SheepRenderState> {

    public SheepDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, EntityType.PIG);
    }

    @Override
    public ResourceLocation getEyesTexture() {
        return ItFollows.id("textures/disguises/sheep_eyes.png");
    }

    @Override
    public SheepRenderState createRenderState() {
        return new SheepRenderState();
    }

    @Override
    public void extractRenderState(TheEntity entity, SheepRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.headEatAngleScale = 0F;
        renderState.headEatPositionScale = 0F;
        renderState.isSheared = false;
        renderState.woolColor = DyeColor.WHITE;
        renderState.id = entity.getId();
    }
}
