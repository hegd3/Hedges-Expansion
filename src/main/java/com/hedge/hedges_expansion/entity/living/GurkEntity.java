package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.HESemiaquaticLookControl;
import com.hedge.hedges_expansion.entity.AI.control.HESemiaquaticMoveControl;
import com.hedge.hedges_expansion.entity.AI.control.HESwimmingLookControl;
import com.hedge.hedges_expansion.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.HECustomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.HEAmphibiousPathNavigator;
import com.hedge.hedges_expansion.entity.types.HEGroupMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class GurkEntity extends Animal implements HEGroupMob<GurkEntity> {

    @Nullable
    private GurkEntity leader;

    private int groupSize = 1;

    public final AnimationState idleAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(GurkEntity.class, EntityDataSerializers.INT);


    public GurkEntity(EntityType<? extends GurkEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);


        this.moveControl = new HESemiaquaticMoveControl(this, 999, 10, 0.25f);
        this.lookControl = new HESemiaquaticLookControl(this, 30);


        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0f);
        this.setMaxUpStep(1.0F);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("Variant"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setVariant(this.getRandom().nextInt(3));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new HEAmphibiousPathNavigator(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 8F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return !this.mob.isInFluidType() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !this.mob.isInFluidType() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(5, new GroupFollowLeaderGoal<>(this));
        this.goalSelector.addGoal(3, new HECustomSwimGoal(this, 1.0, 10, 4, 4, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, LivingEntity.class, 5));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        }
        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends GurkEntity> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(10.0D, 10.0D, 10.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }

    public void setVariant(int i) {
        this.entityData.set(VARIANT, i);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public GurkEntity getLeader() {
        return this.leader;
    }

    @Override
    public void setLeader(GurkEntity leader) {
        this.leader = leader;
    }

    @Override
    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, 1.2f);
        }
    }

    @Override
    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 200.0D;
    }

    @Override
    public int getGroupSize() {
        return this.groupSize;
    }

    @Override
    public int getMaxGroupSize() {
        return 10;
    }

    @Override
    public void addFollower() {
        this.groupSize++;
    }

    @Override
    public void removeFollower() {
        this.groupSize--;
    }

    @Override
    public void addFollowers(Stream<GurkEntity> pFollowers) {
        pFollowers.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter((p_27538_) -> {
            return p_27538_ != this;
        }).forEach((p_27536_) -> {
            if (p_27536_.getVariant() == this.getVariant()) {
                p_27536_.startFollowing(this);
            }
        });
    }
}
