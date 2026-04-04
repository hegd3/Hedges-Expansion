package com.hedge.hedges_expansion.entity.living.ambientfish;

import com.hedge.hedges_expansion.entity.AI.control.HEFlyingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.FlyingWanderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_expansion.entity.types.HESchoolingMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import com.hedge.hedges_expansion.registry.HEEntities;
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
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SpeelEntity extends HESchoolingMob {

    private float prevTrail;
    private float trail = 0.0f;

    public SpeelEntity(EntityType<? extends SpeelEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new HEFlyingMoveControl(this, 999, 20, 1.0f);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.12F);
    }



    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    private void tickTrailYaw() {
        this.prevTrail = this.trail;
        this.trail += (-(this.yBodyRot - this.yBodyRotO) - this.trail) * 0.15F;
    }

    public float getTrailYaw(float partialTick) {
        return (this.prevTrail + (this.trail - this.prevTrail) * partialTick);
    }

    @Override
    protected void clientTick() {
        super.clientTick();
        this.tickTrailYaw();
    }

    protected void handleAirSupply(int pAirSupply) {

    }

    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FlyingWanderGoal(this, 1.0f, 25, 10));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 6, 1.4f, 1.4f));
        this.goalSelector.addGoal(3, new GroupFollowLeaderGoal<>(this));
    }

    @Override
    public int getMaxGroupSize() {
        return 8;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FlyingPathNavigation(this, pLevel);
    }

    public static boolean canSpawn(EntityType<? extends SpeelEntity> entity, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return pos.getY() > 25 && pos.getY() < 70;
    }


    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if ((pReason == MobSpawnType.CHUNK_GENERATION || pReason == MobSpawnType.NATURAL)) {
            int groupSize = (int) (this.getMaxGroupSize() * this.getRandom().nextFloat());
            if (groupSize > 0 && !this.level().isClientSide()) {
                for (int i = 0; i < groupSize; i++) {
                    Vec3 rand = EntityHelpers.getRandomVec3(4);
                    SpeelEntity entity = new SpeelEntity(HEEntities.SPEEL.get(), this.level());
                    entity.moveTo(this.getX() + rand.x, this.getY() + rand.y, this.getZ() + rand.z);
                    entity.startFollowing(this);
                    this.level().addFreshEntity(entity);
                }
            }
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

    }

}
