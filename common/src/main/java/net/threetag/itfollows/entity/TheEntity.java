package net.threetag.itfollows.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.itfollows.attachment.IFAttachments;
import net.threetag.itfollows.entity.ai.goal.FollowTargetGoal;
import net.threetag.itfollows.entity.disguise.DisguiseType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.UUID;

public class TheEntity extends PathfinderMob {

    private Player targetPlayer;
    private int ticksSinceDisguiseUpdate;
    private long ticketTimer = 0L;

    public TheEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public TheEntity(Player target) {
        this(IFEntityTypes.THE_ENTITY.get(), target.level());
        this.setTargetId(target.getUUID());
        this.targetPlayer = target;
        this.setPos(target.position());
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return PathfinderMob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 666);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FollowTargetGoal(this));
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
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {

            // fix sync issue
            if (this.tickCount <= 1) {
                var target = this.getTargetId();
                this.setTargetId(null);
                this.setTargetId(target);
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

                    for (DisguiseType disguiseType : ImmutableList.copyOf(DisguiseType.REGISTRY).stream()
                            .sorted(Comparator.comparingInt(DisguiseType::getPriority).reversed()).toList()) {
                        if (disguiseType.isValid(this)) {
                            this.setDisguiseType(disguiseType);
                            break;
                        }
                    }
                } else {
                    this.discard();
                }

                this.ticksSinceDisguiseUpdate = 0;
            }

            if (this.isAlive()) {
                BlockPos blockPos = BlockPos.containing(this.position());
                int i = SectionPos.blockToSectionCoord(this.position().x());
                int j = SectionPos.blockToSectionCoord(this.position().z());

                if ((--this.ticketTimer <= 0L || i != SectionPos.blockToSectionCoord(blockPos.getX()) || j != SectionPos.blockToSectionCoord(blockPos.getZ()))
                        && this.getTargetPlayer() instanceof ServerPlayer serverPlayer) {
                    var handler = CursePlayerHandler.get(serverPlayer);
                    this.ticketTimer = handler.registerAndUpdateTicket(this);
                }
            }
        }
    }

    @Override
    public void onRemoval(RemovalReason removalReason) {
        super.onRemoval(removalReason);
        var target = this.getTargetPlayer();

        if (target instanceof ServerPlayer player) {
            CursePlayerHandler.get(player).removeEntity(this);
        }
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

    public void setTargetId(UUID targetId) {
        IFAttachments.TARGET_ID.set(this, targetId);
    }

    public UUID getTargetId() {
        return IFAttachments.TARGET_ID.get(this);
    }

    private void setDisguiseType(DisguiseType disguiseType) {
        IFAttachments.DISGUISE_TYPE.set(this, disguiseType);
    }

    public DisguiseType getDisguiseType() {
        return IFAttachments.DISGUISE_TYPE.get(this);
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return player != this.targetPlayer;
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
    public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource damageSource) {
        if (damageSource.getEntity() instanceof Player player) {
            return !player.isCreative();
        }

        return true;
    }

    public static Vec3 getRandomPos(Level level, Vec3 center, int distance, RandomSource random) {
        Vec3 distanced = new Vec3(distance, 0, 0).yRot((float) Math.toRadians(random.nextInt(360)));
        var pos = new BlockPos((int) center.x(), (int) center.y(), (int) center.z()).offset((int) distanced.x, 0, (int) distanced.z);

        while (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                || level.getBlockState(pos.below()).getCollisionShape(level, pos).isEmpty()) {
            pos = pos.below();

            if (pos.getY() <= 0) {
                return pos.offset(0, (int) center.y, 0).getBottomCenter();
            }
        }

        return pos.getBottomCenter();
    }
}
