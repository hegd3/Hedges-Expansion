package com.hedge.hedges_bestiary.blocks;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.util.BlockHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class EggBlock extends Block {

    public static final VoxelShape LARGE_EGG = BlockHelpers.createRectangular(10, 14);
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    protected final Supplier<? extends EntityType> toHatch;
    private final VoxelShape shape;
    private final TagKey<Block> preferredBlock;
    private final int hatchInterval;

    public EggBlock(Properties properties, Supplier<? extends EntityType> toHatch, TagKey<Block> preferredBlock, VoxelShape shape) {
        this(properties, toHatch, preferredBlock, shape, 10);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    public EggBlock(Properties properties, Supplier<? extends EntityType> toHatch, TagKey<Block> preferredBlock, VoxelShape shape, int hatchInterval) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, 0));

        this.toHatch = toHatch;
        this.preferredBlock = preferredBlock;
        this.shape = shape;
        this.hatchInterval = hatchInterval;
    }



    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }

    public int getHatchLevel(BlockState state) {
        return state.getValue(HATCH);
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.shouldUpdateHatchLevel(level, pos.below()) && state.is(this)) {
            int progress = state.getValue(HATCH);

            if (progress < 2) {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.setBlock(pos, state.setValue(HATCH, this.getHatchLevel(state) + 1), 2);
                level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            } else {

                this.hatch(level, pos);

                level.playSound(null, pos, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.removeBlock(pos, false);
                level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));

                level.levelEvent(2001, pos, Block.getId(state));

            }
        }
    }

    protected void hatch(ServerLevel level, BlockPos pos) {
        Entity hatched = toHatch.get().create(level);
        Vec3 vec3 = pos.getCenter();
        hatched.moveTo(vec3.x(), vec3.y(), vec3.z(), Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
        if (hatched instanceof HBTamableAnimal baby) {
            baby.setAge(-24000);
            baby.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.BREEDING, null, null);
            if (!level.isClientSide()) {
                Player closest = level.getNearestPlayer(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 10, EntitySelector.NO_SPECTATORS);
                if (closest != null) {
                    baby.tame(closest);
                    baby.setOrderedToSit(true);
                    baby.setTame(true);
                }
            }
        }
        level.addFreshEntity(hatched);
    }

    private boolean shouldUpdateHatchLevel(Level level, BlockPos ground) {
        return level.random.nextInt(level.getBlockState(ground).is(preferredBlock) ? this.hatchInterval / 2 : this.hatchInterval) == 0;
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        boolean flag = hatchBoost(level, pos);
        if (!level.isClientSide() && flag) {
            level.levelEvent(3009, pos, 0);
        }

        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(state));
    }


    public boolean hatchBoost(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(this.preferredBlock);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }
}
