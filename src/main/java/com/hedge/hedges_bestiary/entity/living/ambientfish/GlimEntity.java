package com.hedge.hedges_bestiary.entity.living.ambientfish;

import com.hedge.hedges_bestiary.entity.AI.control.SwimmingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.CustomSwimGoal;
import com.hedge.hedges_bestiary.entity.living.FerocetusEntity;
import com.hedge.hedges_bestiary.entity.types.HBSchoolingMob;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GlimEntity extends HBSchoolingMob {

    public GlimEntity(EntityType<? extends GlimEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SwimmingMoveControl(this, 999, 20, 0.02f, 0.1f);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.95F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new GroupFollowLeaderGoal<>(this));
        this.goalSelector.addGoal(1, new CustomSwimGoal(this, 1.0f, 10, 6, 10, false));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, FerocetusEntity.class, 10, 1.4f, 1.4f));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6, 1.4f, 1.4f));
    }

    @Override
    public int getMaxGroupSize() {
        return 25;
    }

    @Override
    public void aiStep() {
        this.flop();
        super.aiStep();
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }



    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    public static boolean canSpawn(EntityType<GlimEntity> entity, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(entity, level, reason, pos, random);
    }




}
