package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.HESwimmingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.HECustomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.goal.JumpFromWaterGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.FluidPathNavigation;
import com.hedge.hedges_expansion.entity.types.HESchoolingMob;
import com.hedge.hedges_expansion.entity.util.AttackStateMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import com.hedge.hedges_expansion.registry.HEEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FerocetusEntity extends HESchoolingMob implements AttackStateMob {
    public float tilt = 0.0f;
    public int groundTimer = 0;



    private int attackCD = 0;

    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState ramAnimationState = new AnimationState();

    public static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(FerocetusEntity.class, EntityDataSerializers.BOOLEAN);

    public FerocetusEntity(EntityType<? extends FerocetusEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new HESwimmingMoveControl(this, 999, 7, 0.02f, 0.1f);
        this.lookControl = new SmoothSwimmingLookControl(this, 7);
    }

    @Override
    public int getMaxGroupSize() {
        return 6;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.8F);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(3, new JumpFromWaterGoal(this, 20, 0.9));
        this.goalSelector.addGoal(4, new HECustomSwimGoal(this, 1.0f, 25, 5, 3, true));
        this.goalSelector.addGoal(5, new GroupFollowLeaderGoal<>(this));

    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInFluidType()) {
            final float v = Mth.degreesDifference(this.getYRot(), yRotO);
            if (Math.abs(v) > 1) {
                if (Math.abs(tilt) < 25)
                {
                    tilt -= Math.signum(v);
                }
            } else {
                if (Math.abs(tilt) > 0)
                { final float tiltSign = Math.signum(tilt);
                    tilt -= tiltSign * 0.85F;
                    if (tilt * tiltSign < 0)
                    { tilt = 0; }
                }
            }
        }
        else {
            tilt = 0;
            if (this.onGround()) {
                this.groundTimer = 20;
            } else {
                this.groundTimer = Math.max(groundTimer - 1, 0);
            }
        }
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.ramAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);

    }

    @Override
    protected void serverTick() {
        super.serverTick();
        this.attackCD = Math.max(this.attackCD - 1, 0);
        if (this.tickCount % 200 == 0) {
            this.heal(10);
        }
    }

    @Override
    public void setAttacking() {
        this.setAnimState(this.getRandom().nextInt(2) + 1);
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.2 * this.getBbWidth() * 2.2 + entity.getBbWidth();
    }

    @Override
    public void pathToLeader() {
        if (this.isFollower()) {
            Vec3 pos = this.leader.position().add(0, 5 * this.getRandom().nextDouble() - 5 * this.getRandom().nextDouble(), 0);
            this.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.2f);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FluidPathNavigation(this, pLevel);
    }

    public static boolean canSpawn(EntityType<FerocetusEntity> entity, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(entity, level, reason, pos, random);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if ((pReason == MobSpawnType.CHUNK_GENERATION || pReason == MobSpawnType.NATURAL)) {
            int groupSize = (int) (this.getMaxGroupSize() * this.getRandom().nextFloat());
            if (groupSize > 0 && !this.level().isClientSide()) {
                for (int i = 0; i < groupSize; i++) {
                    Vec3 rand = EntityHelpers.getRandomVec3(6);
                    FerocetusEntity entity = new FerocetusEntity(HEEntities.FEROCETUS.get(), this.level());
                    entity.moveTo(this.getX() + rand.x, this.getY() + rand.y, this.getZ() + rand.z);
                    entity.startFollowing(this);
                    this.level().addFreshEntity(entity);
                }
            }
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

    }
}
