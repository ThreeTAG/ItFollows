package net.threetag.itfollows.entity;

import com.google.common.collect.ImmutableList;
import dev.architectury.extensions.network.EntitySpawnExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.threetag.itfollows.entity.ai.goal.FollowTargetGoal;
import net.threetag.itfollows.entity.disguise.DisguiseType;
import net.threetag.itfollows.entity.disguise.DisguiseTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.UUID;

public class TheEntity extends PathfinderMob implements EntitySpawnExtension {

    private static final EntityDataAccessor<DisguiseType> DISGUISE = SynchedEntityData.defineId(TheEntity.class, DisguiseType.ENTITY_DATA);

    private UUID targetId;
    private Player targetPlayer;
    private int ticksSinceDisguiseUpdate;
    private long ticketTimer = 0L;

    public TheEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public TheEntity(Player target, int distance) {
        this(IFEntityTypes.THE_ENTITY.get(), target.level());
        this.targetId = target.getUUID();
        this.targetPlayer = target;
        this.setPos(getRandomPos(target.position(), distance, this.random));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return PathfinderMob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 666);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DISGUISE, DisguiseTypes.PIG.get());
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
            this.ticksSinceDisguiseUpdate++;

            if (this.ticksSinceDisguiseUpdate > 200) {
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
        if ((this.targetPlayer == null || this.targetPlayer.isRemoved()) && this.targetId != null) {
            this.targetPlayer = this.level().getPlayerByUUID(this.targetId);
        }

        return this.targetPlayer;
    }

    private void setDisguiseType(DisguiseType disguiseType) {
        this.entityData.set(DISGUISE, disguiseType);
    }

    public DisguiseType getDisguiseType() {
        return this.entityData.get(DISGUISE);
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return player != this.targetPlayer;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        if (compoundTag.contains("target")) {
            this.targetId = UUIDUtil.CODEC.parse(NbtOps.INSTANCE, compoundTag.get("target")).getOrThrow();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        if (this.targetId != null) {
            compoundTag.put("target", UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, this.targetId).getOrThrow());
        }
    }

    @Override
    public void saveAdditionalSpawnData(FriendlyByteBuf buf) {
        this.targetId = buf.readNullable(b -> b.readUUID());
    }

    @Override
    public void loadAdditionalSpawnData(FriendlyByteBuf buf) {
        buf.writeNullable(this.targetId, (b, id) -> b.writeUUID(id));
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
        var pos = new BlockPos(
                (int) (center.x() + random.nextIntBetweenInclusive(-distance, distance)),
                (int) center.y(),
                (int) (center.z() + random.nextIntBetweenInclusive(-distance, distance))
        );

        while (!level.getBlockState(pos.below()).getCollisionShape(level, pos).isEmpty()
                || level.getBlockState(pos.below()).getCollisionShape(level, pos).isEmpty()) {
            pos = pos.above();

            if (pos.getY() >= level.getHeight()) {
                return pos.getBottomCenter();
            }
        }

        return pos.getBottomCenter();
    }
}
