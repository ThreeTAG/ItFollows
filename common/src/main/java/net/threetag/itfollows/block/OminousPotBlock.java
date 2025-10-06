package net.threetag.itfollows.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class OminousPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<OminousPotBlock> CODEC = simpleCodec(OminousPotBlock::new);
    private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 16.0);
    public static final IntegerProperty USAGE = IntegerProperty.create("usage", 0, 3);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public OminousPotBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(USAGE, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof OminousPotBlockEntity pot && !level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (state.getValue(USAGE) < 3) {
                if (pot.getLootTable() == null) {
                    pot.setLootTable(OminousPotBlockEntity.DEFAULT_LOOT_TABLE, RandomSource.create().nextLong());
                }

                pot.unpackLootTable(player);
                int currentUsage = state.getValue(USAGE);
                int usage = pot.givePlayerItem(serverPlayer, currentUsage);

                if (currentUsage != usage) {
                    if (usage > 3) {
                        level.playSeededSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundType.DECORATED_POT_CRACKED.getBreakSound()), player.getSoundSource(), 1.0F, 1.0F, RandomSource.create().nextLong());
                    }

                    level.setBlock(pos, state.setValue(USAGE, usage), 3);
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    return InteractionResult.SUCCESS_SERVER;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                // TODO free curse
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(USAGE, 0);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, USAGE);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected SoundType getSoundType(BlockState state) {
        return state.getValue(USAGE) >= 3 ? SoundType.DECORATED_POT_CRACKED : SoundType.DECORATED_POT;
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (level instanceof ServerLevel serverLevel && projectile.mayInteract(serverLevel, blockPos) && projectile.mayBreak(serverLevel)) {
            level.setBlock(blockPos, state.setValue(USAGE, 3), 260);
            level.destroyBlock(blockPos, true, projectile);
            // TODO free curse
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(USAGE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OminousPotBlockEntity(pos, state);
    }
}
