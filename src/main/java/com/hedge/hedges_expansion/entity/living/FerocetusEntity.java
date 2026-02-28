package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.HESwimmingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.FerocetusAttackGoal;
import com.hedge.hedges_expansion.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.HECustomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.FluidPathNavigation;
import com.hedge.hedges_expansion.entity.living.ambientfish.GlimEntity;
import com.hedge.hedges_expansion.entity.projectile.WaveEntity;
import com.hedge.hedges_expansion.entity.types.HESchoolingMob;
import com.hedge.hedges_expansion.entity.util.AttackHelpers;
import com.hedge.hedges_expansion.entity.types.AttackStateMob;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FerocetusEntity extends HESchoolingMob implements AttackStateMob {

    private float prevTrail;
    private float trail = 0.0f;


    public int groundTimer = 0;


    private int jumpCD = 0;
    private int attackCD = 0;
    private boolean leftWater = false;


    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState ramAnimationState = new AnimationState();

    public static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(FerocetusEntity.class, EntityDataSerializers.BOOLEAN);

    public FerocetusEntity(EntityType<? extends FerocetusEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new HESwimmingMoveControl(this, 999, 7, 0.02f, 0.0f);
        this.lookControl = new SmoothSwimmingLookControl(this, 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof FerocetusEntity && pEntity.getTeam() == this.getTeam()) {
            return true;
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    public int getMaxGroupSize() {
        return 6;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.4D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.8F);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FerocetusAttackGoal(this));
        this.goalSelector.addGoal(4, new HECustomSwimGoal(this, 1.0f, 25, 5, 3, true));
        this.goalSelector.addGoal(5, new GroupFollowLeaderGoal<>(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, GlimEntity.class, true));


    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround()) {
            this.groundTimer = 20;
        } else {
            this.groundTimer = Math.max(groundTimer - 1, 0);

            if (!this.level().isClientSide() && this.groundTimer == 0) {
                Vec3 vec3 = this.getDeltaMovement();
                if (vec3.y * vec3.y < (double) 0.03F && this.getXRot() != 0.0F) {
                    this.setXRot(Mth.rotLerp(0.2F, this.getXRot(), 0.0F));
                } else if (vec3.length() > (double) 1.0E-5F) {
                    double d0 = vec3.horizontalDistance();
                    double d1 = Math.atan2(-vec3.y, d0) * (double) (180F / (float) Math.PI);
                    this.setXRot((float) d1);
                }
            }
        }

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
        this.jumpCD = Math.max(this.jumpCD - 1, 0);
        if (this.tickCount % 200 == 0) {
            this.heal(10);
        }
        if (this.getAnimState() > 0) {
            this.animTicks++;
            switch (this.getAnimState()) {
                case 1 -> {
                    if (this.animTicks == 13) {
                        for (LivingEntity entity : AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this), 2, 2, 2, 5)) {
                            this.doHurtTarget(entity);
                        }
                    } else if (this.animTicks >= 23) {
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    if (this.animTicks == 11) {
                        for (LivingEntity entity : AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this), 2, 2, 2, 5)) {
                            if (AttackHelpers.betterHurt(this, entity, 0.8f, 1.2f)) {
                                EntityHelpers.knockUp(entity, 1.2);
                            }
                        }
                    } else if (this.animTicks >= 22) {
                        this.resetAnimState();
                    }
                }
                case 3 -> {
                    if (!this.leftWater && !this.isInFluidType()) {
                        this.leftWater = true;
                    } else if (this.leftWater && this.isInFluidType()) {
                        for (int i = -180; i < 180; i+= 60) {
                            this.createWave(i);
                        }
                        this.jumpCD = 60;
                        this.resetAnimState();
                    } else if (this.onGround() || (this.animTicks >= 60 && this.isInFluidType())) {
                        this.jumpCD = 60;
                        this.resetAnimState();
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        if (this.getAnimState() == 3) {
            return false;
        }
        return super.isPushable();
    }

    private void createWave(int i) {
        WaveEntity entity = HEEntities.WAVE.get().create(this.level());
        entity.shoot(this, this.getYRot() + i);
        this.level().addFreshEntity(entity);
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD = 5;
    }

    @Override
    public void setAttacking() {
        int i = this.getRandom().nextInt(2) + 1;
        this.setAnimState(i);
        if (i == 1) {
            this.setLeft(!this.swingingLeft());
        }
    }

    public boolean canJump(double attackReach, double dist) {
        if (this.jumpCD == 0 && attackReach * 5 >= dist && EntityHelpers.closeToSurface(this, 5)) {
            this.leftWater = false;
            return true;
        }
        return false;
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

    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    private void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }
}
