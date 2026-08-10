package com.hedge.hedges_bestiary.blocks;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.util.BlockHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MultiEggBlock extends EggBlock {

    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;
    private static final VoxelShape SINGULAR_EGG = BlockHelpers.createRectangular(10, 9);
    private static final VoxelShape MULTIPLE_EGGS = BlockHelpers.createRectangular(15, 9);

    public MultiEggBlock(Properties properties, Supplier<? extends EntityType> toHatch, TagKey<Block> preferredBlock) {
        super(properties, toHatch, preferredBlock, SINGULAR_EGG);
        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, 0).setValue(EGGS, 1));

    }

    public MultiEggBlock(Properties properties, Supplier<? extends EntityType> toHatch, TagKey<Block> preferredBlock, int hatchInterval) {
        super(properties, toHatch, preferredBlock, SINGULAR_EGG, hatchInterval);
        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, 0).setValue(EGGS, 1));

    }



    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.getItemInHand().getItem() == this.asItem() && state.getValue(EGGS) < 4 || super.canBeReplaced(state, useContext);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        return blockstate.getBlock() == this ? blockstate.setValue(EGGS, Integer.valueOf(Math.min(4, blockstate.getValue(EGGS) + 1))) : super.getStateForPlacement(context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(EGGS) > 1 ? MULTIPLE_EGGS : SINGULAR_EGG;
    }

    @Override
    protected void hatch(ServerLevel level, BlockPos pos) {
        for (int i = 1; i <= level.getBlockState(pos).getValue(EGGS); i++) {
            Entity hatched = toHatch.get().create(level);
            Vec3 vec3 = pos.getCenter();
            hatched.moveTo(vec3.x() + level.random.nextFloat() * 0.5, vec3.y(), vec3.z() + level.random.nextFloat() * 0.5, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
            if (hatched instanceof HBTamableAnimal baby) {
                baby.setAge(-24000);
                baby.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.BREEDING, null, null);
                if (!level.isClientSide()) {
                    Player closest = level.getNearestPlayer(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 10, EntitySelector.NO_SPECTATORS);
                    if (closest != null) {
                        baby.tame(closest);
                        baby.setOrderedToSit(true);
                        baby.setCommand(1);
                        baby.setTame(true);
                    }
                }
            }
            level.addFreshEntity(hatched);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EGGS);
    }
}
