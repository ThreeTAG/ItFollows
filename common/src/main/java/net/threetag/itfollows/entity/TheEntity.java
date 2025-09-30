package net.threetag.itfollows.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.threetag.itfollows.IFConfig;
import net.threetag.itfollows.attachment.IFAttachments;
import net.threetag.itfollows.entity.ai.goal.FollowTargetGoal;
import net.threetag.itfollows.entity.ai.goal.KillTargetGoal;
import net.threetag.itfollows.entity.disguise.DisguiseType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TheEntity extends PathfinderMob {

    private Player targetPlayer;
    private Player targetInfectorPlayer;
    private int ticksSinceDisguiseUpdate;
    private boolean markedForRemoval = false;

    public TheEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public TheEntity(Player target) {
        this(IFEntityTypes.THE_ENTITY.get(), target.level());
        this.setTargetId(target.getUUID());
        this.targetPlayer = target;

        if (target instanceof ServerPlayer serverPlayer) {
            var handler = CursePlayerHandler.get(serverPlayer);
            this.setTargetInfectorId(handler.getInfectedBy());
        }

        this.setPos(target.position());
        this.moveControl = new TheEntityMoveControl(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.getNavigation().setCanOpenDoors(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, IFConfig.MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, 666)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new KillTargetGoal(this));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, false));
        this.goalSelector.addGoal(3, new FollowTargetGoal(this));
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isUnderWater()) {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            this.setSwimming(this.isInWater());
        }
    }

    @Override
    public boolean isVisuallySwimming() {
        return this.isSwimming();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.markedForRemoval) {
                this.discard();
                return;
            }

            // set full health
            this.setHealth(this.getMaxHealth());

            // fix sync issue
            if (this.tickCount <= 10) {
                var target = this.getTargetId();
                var targetInfector = this.getTargetInfectorId();
                this.setTargetId(null);
                this.setTargetId(target);
                this.setTargetInfectorId(null);
                this.setTargetInfectorId(targetInfector);
            }

            this.ticksSinceDisguiseUpdate++;

            if (this.ticksSinceDisguiseUpdate > 200 || this.getDisguiseType() == null) {
                var target = this.getTargetPlayer();

                if (target instanceof ServerPlayer player) {
                    var handler = CursePlayerHandler.get(player);
                    if (!handler.setLastKnownEntity(this) || !handler.isCurseActive()) {
                        this.discard();
                        return;
                    }

                    if (!this.isLookingAtMe(target, 0.5, false, true, this.getY(), this.getEyeY(), this.getY() + this.getBbHeight())) {
                        this.setDisguiseType(DisguiseType.findDisguise(this, RandomSource.create()));
                    }
                } else {
                    this.discard();
                }

                this.ticksSinceDisguiseUpdate = 0;
            }
        }
    }

    @Override
    public void onRemoval(RemovalReason removalReason) {
        super.onRemoval(removalReason);
        this.markedForRemoval = true;
        var target = this.getTargetPlayer();

        if (target instanceof ServerPlayer player) {
            CursePlayerHandler.get(player).removeEntity(this);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.markedForRemoval = input.getBooleanOr("marked_for_removal", false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("marked_for_removal", this.markedForRemoval);
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Nullable
    public Player getTargetPlayer() {
        if ((this.targetPlayer == null || this.targetPlayer.isRemoved()) && this.getTargetId() != null) {
            this.targetPlayer = this.level().getPlayerByUUID(this.getTargetId());
        }

        return this.targetPlayer;
    }

    @Nullable
    public Player getTargetInfectorPlayer() {
        if ((this.targetInfectorPlayer == null || this.targetInfectorPlayer.isRemoved()) && this.getTargetInfectorId() != null) {
            this.targetInfectorPlayer = this.level().getPlayerByUUID(this.getTargetInfectorId());
        }

        return this.targetInfectorPlayer;
    }

    public void setTargetId(UUID targetId) {
        IFAttachments.TARGET_ID.set(this, targetId);
    }

    public UUID getTargetId() {
        return IFAttachments.TARGET_ID.get(this);
    }

    public void setTargetInfectorId(UUID targetId) {
        IFAttachments.TARGET_INFECTOR_ID.set(this, targetId);
    }

    public UUID getTargetInfectorId() {
        return IFAttachments.TARGET_INFECTOR_ID.get(this);
    }

    private void setDisguiseType(DisguiseType disguiseType) {
        IFAttachments.DISGUISE_TYPE.set(this, disguiseType);
    }

    public DisguiseType getDisguiseType() {
        return IFAttachments.DISGUISE_TYPE.get(this);
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return player != this.getTargetPlayer() && player != this.getTargetInfectorPlayer();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level() instanceof ServerLevel && this.isInWater()) {
            this.setAirSupply(300);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        amount = 0.1F;
        return super.hurtServer(level, damageSource, amount);
    }

    public static Vec3 getRandomPos(Level level, Vec3 center, int distance, RandomSource random) {
        Vec3 distanced = new Vec3(distance, 0, 0).yRot((float) Math.toRadians(random.nextInt(360)));
        var pos = new BlockPos((int) center.x(), level.getHeight(), (int) center.z()).offset((int) distanced.x, 0, (int) distanced.z);

        while (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                || level.getBlockState(pos.below()).getCollisionShape(level, pos).isEmpty()) {
            pos = pos.below();

            if (pos.getY() <= 0) {
                return pos.offset(0, (int) center.y, 0).getBottomCenter();
            }
        }

        return pos.getBottomCenter();
    }

    static class TheEntityMoveControl extends MoveControl {

        private final TheEntity drowned;

        public TheEntityMoveControl(TheEntity drowned) {
            super(drowned);
            this.drowned = drowned;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.drowned.getTarget();
            if (this.drowned.isInWater()) {
                if (livingEntity != null && livingEntity.getY() > this.drowned.getY()) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, 0.002, 0.0));
                }

                if (this.drowned.getNavigation().isDone()) {
                    this.drowned.setSpeed(0.0F);
                    return;
                }

                double d = this.wantedX - this.drowned.getX();
                double e = this.wantedY - this.drowned.getY();
                double f = this.wantedZ - this.drowned.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (Mth.atan2(f, d) * 180.0F / (float) Math.PI) - 90.0F;
                this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), h, 90.0F));
                this.drowned.yBodyRot = this.drowned.getYRot();
                float i = (float) (this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125F, this.drowned.getSpeed(), i);
                this.drowned.setSpeed(j * 2);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(j * d * 0.005, j * e * 0.1, j * f * 0.005));
                super.tick();
            } else {
                super.tick();
            }
        }
    }
}
