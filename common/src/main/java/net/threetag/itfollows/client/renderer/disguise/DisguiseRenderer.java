package net.threetag.itfollows.client.renderer.disguise;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.threetag.itfollows.client.renderer.layer.GlowingEyesLayer;
import net.threetag.itfollows.entity.TheEntity;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DisguiseRenderer<S extends LivingEntityRenderState> {

    public final S reusedState;
    private final EntityRenderer<?, ? super S> entityRenderer;
    private final GlowingEyesLayer glowingEyesLayer;
    protected final ItemModelResolver itemModelResolver;

    public DisguiseRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        this.reusedState = this.createRenderState();
        this.reusedState.entityType = entityType;
        this.entityRenderer = context.getEntityRenderDispatcher().getRenderer(this.reusedState);
        this.itemModelResolver = context.getItemModelResolver();

        if (this.entityRenderer instanceof LivingEntityRenderer<?, ?, ?> livingEntityRenderer) {
            livingEntityRenderer.addLayer(this.glowingEyesLayer = new GlowingEyesLayer(livingEntityRenderer, this.getEyesTexture()));
        } else {
            this.glowingEyesLayer = null;
        }
    }

    public abstract S createRenderState();

    public abstract ResourceLocation getEyesTexture();

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.glowingEyesLayer != null) {
            this.glowingEyesLayer.enabled = true;
        }

        this.entityRenderer.render(this.reusedState, poseStack, bufferSource, packedLight);

        if (this.glowingEyesLayer != null) {
            this.glowingEyesLayer.enabled = false;
        }
    }

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
    }

    private static float solveBodyRot(LivingEntity livingEntity, float f, float g) {
        if (livingEntity.getVehicle() instanceof LivingEntity livingEntity2) {
            float h = Mth.rotLerp(g, livingEntity2.yBodyRotO, livingEntity2.yBodyRot);
            float i = 85.0F;
            float j = Mth.clamp(Mth.wrapDegrees(f - h), -i, i);
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
