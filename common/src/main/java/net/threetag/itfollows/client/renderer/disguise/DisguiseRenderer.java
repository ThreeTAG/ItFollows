package net.threetag.itfollows.client.renderer.disguise;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.threetag.itfollows.entity.TheEntity;

public abstract class DisguiseRenderer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    public final S reusedState = this.createRenderState();
    protected final ItemModelResolver itemModelResolver;

    public DisguiseRenderer(EntityRendererProvider.Context context) {
        this.itemModelResolver = context.getItemModelResolver();
    }

    public abstract M getModel();

    public abstract ResourceLocation getTexture();

    public void setupRotations(S renderState, PoseStack poseStack, float bodyRot, float scale) {
        if (!renderState.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyRot));
        }

        if (renderState.deathTime > 0.0F) {
            float f = (renderState.deathTime - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            poseStack.mulPose(Axis.ZP.rotationDegrees(f * 90F));
        } else if (renderState.isAutoSpinAttack) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - renderState.xRot));
            poseStack.mulPose(Axis.YP.rotationDegrees(renderState.ageInTicks * -75.0F));
        } else if (renderState.isUpsideDown) {
            poseStack.translate(0.0F, (renderState.boundingBoxHeight + 0.1F) / scale, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    public abstract S createRenderState();

    public void extractRenderState(TheEntity entity, S renderState, float partialTick) {
        float g = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
        renderState.bodyRot = solveBodyRot(entity, g, partialTick);
        renderState.yRot = Mth.wrapDegrees(g - renderState.bodyRot);
        renderState.xRot = entity.getXRot(partialTick);
        renderState.customName = entity.getCustomName();
        renderState.isUpsideDown = false;

        if (!entity.isPassenger() && entity.isAlive()) {
            renderState.walkAnimationPos = entity.walkAnimation.position(partialTick);
            renderState.walkAnimationSpeed = entity.walkAnimation.speed(partialTick);
        } else {
            renderState.walkAnimationPos = 0.0F;
            renderState.walkAnimationSpeed = 0.0F;
        }

        if (entity.getVehicle() instanceof LivingEntity livingEntity2) {
            renderState.wornHeadAnimationPos = livingEntity2.walkAnimation.position(partialTick);
        } else {
            renderState.wornHeadAnimationPos = renderState.walkAnimationPos;
        }

        renderState.scale = entity.getScale();
        renderState.ageScale = entity.getAgeScale();
        renderState.pose = entity.getPose();
        renderState.bedOrientation = entity.getBedOrientation();
        if (renderState.bedOrientation != null) {
            renderState.eyeHeight = entity.getEyeHeight(Pose.STANDING);
        }

        renderState.isFullyFrozen = entity.isFullyFrozen();
        renderState.isBaby = entity.isBaby();
        renderState.isInWater = entity.isInWater();
        renderState.isAutoSpinAttack = entity.isAutoSpinAttack();
        renderState.hasRedOverlay = entity.hurtTime > 0 || entity.deathTime > 0;
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock abstractSkullBlock) {
            renderState.wornHeadType = abstractSkullBlock.getType();
            renderState.wornHeadProfile = itemStack.get(DataComponents.PROFILE);
            renderState.headItem.clear();
        } else {
            renderState.wornHeadType = null;
            renderState.wornHeadProfile = null;
            if (!HumanoidArmorLayer.shouldRender(itemStack, EquipmentSlot.HEAD)) {
                this.itemModelResolver.updateForLiving(renderState.headItem, itemStack, ItemDisplayContext.HEAD, entity);
            } else {
                renderState.headItem.clear();
            }
        }

        renderState.deathTime = entity.deathTime > 0 ? entity.deathTime + partialTick : 0.0F;
        Minecraft minecraft = Minecraft.getInstance();
        renderState.isInvisibleToPlayer = renderState.isInvisible && entity.isInvisibleTo(minecraft.player);
        renderState.appearsGlowing = minecraft.shouldEntityAppearGlowing(entity);
    }

    private static float solveBodyRot(LivingEntity livingEntity, float f, float g) {
        if (livingEntity.getVehicle() instanceof LivingEntity livingEntity2) {
            float h = Mth.rotLerp(g, livingEntity2.yBodyRotO, livingEntity2.yBodyRot);
            float i = 85.0F;
            float j = Mth.clamp(Mth.wrapDegrees(f - h), -85.0F, 85.0F);
            h = f - j;
            if (Math.abs(j) > 50.0F) {
                h += j * 0.2F;
            }

            return h;
        } else {
            return Mth.rotLerp(g, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        }
    }

}
