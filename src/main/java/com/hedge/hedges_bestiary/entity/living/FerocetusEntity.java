package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.entity.AI.control.SwimmingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.targeting.HBHurtByTargetGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.IdleAnimationGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.FerocetusAttackGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.CustomSwimGoal;
import com.hedge.hedges_bestiary.entity.AI.navigation.FluidPathNavigation;
import com.hedge.hedges_bestiary.entity.types.HBSchoolingMob;
import com.hedge.hedges_bestiary.entity.types.IdleAnimMob;
import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.types.AttackStateMob;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FerocetusEntity extends HBSchoolingMob implements AttackStateMob, IdleAnimMob {

    private float prevTrail;
    private float trail = 0.0f;


    public int groundTimer = 0;


    private int jumpCD = 0;
    private int attackCD = 0;
    private boolean leftWater = false;

    public final SmoothAnimationState biteAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState ramAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState airAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState spinAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState callAnimationState = new SmoothAnimationState();


    public static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(FerocetusEntity.class, EntityDataSerializers.BOOLEAN);

    public FerocetusEntity(EntityType<? extends FerocetusEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SwimmingMoveControl(this, 999, 7, 0.02f, 0.0f);
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
                .add(Attributes.MAX_HEALTH, 90.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.4D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.8F);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FerocetusAttackGoal(this));
        this.goalSelector.addGoal(1, new GroupFollowLeaderGoal<>(this));
        this.goalSelector.addGoal(2, new CustomSwimGoal(this, 1.0f, 25, 5, 3, true));
        this.goalSelector.addGoal(3, new IdleAnimationGoal<>(this));

        this.targetSelector.addGoal(0, new HBHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, TearacudaEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, MurkEntity.class, true));


    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround()) {
            this.groundTimer = 20;
        } else {
            this.groundTimer = Math.max(groundTimer - 1, 0);

            if (!this.level().isClientSide() && this.groundTimer == 0 && !this.isInFluidType()) {
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
    protected void clientTick() {
        super.clientTick();
        this.tickTrailYaw();
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.ramAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.spinAnimationState.animateWhen(this.getAnimState() == 4, this.tickCount);
        this.callAnimationState.animateWhen(this.getAnimState() == 5, this.tickCount);
        this.airAnimationState.animateWhen(this.groundTimer == 0 && !this.isInFluidType(), this.tickCount);
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
                        for (LivingEntity entity : AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this, this.getXRot()).scale(1.3), 2.5, 2.5, 2.5, 5)) {
                            this.doHurtTarget(entity);
                        }
                    } else if (this.animTicks >= 23) {
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    this.getNavigation().stop();
                    if (this.animTicks <= 10) {
                        LivingEntity target = this.getTarget();
                        if (target != null) {
                            this.lookAt(target, 15f, 30f);
                            this.getLookControl().setLookAt(target, 15f, 30f);
                        }
                    }
                    else if (this.animTicks == 20) {
                        this.addDeltaMovement(EntityHelpers.bodyAngle(this, this.getXRot() * 4 - 20).scale(1.1));
                    }
                    else if (this.animTicks == 24) {
                        for (LivingEntity entity : AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this, this.getXRot()).scale(1.4), 3, 3, 3, 10)) {
                            if (AttackHelpers.betterHurt(this, entity, 1.8f, 1.8f)) {
                                EntityHelpers.knockUp(entity, 1.5);
                            }
                        }
                    } else if (this.animTicks >= 45) {
                        this.resetAnimState();
                    } else {
                        this.setXRot(Mth.approachDegrees(this.getXRot(), 0, 10));
                    }
                }
                case 3 -> {
                    if (!this.leftWater && !this.isInFluidType()) {
                        this.leftWater = true;
                    } else if (this.leftWater && this.isInFluidType()) {
                        /*
                        for (int i = -180; i < 180; i+= 60) {
                            this.createWave(i);
                        }

                         */
                        for (LivingEntity entity : AttackHelpers.zoneHitbox(this, Vec3.ZERO, 4, 4, 4, 10)) {
                            if (AttackHelpers.betterHurt(this, entity, 1.3f, 1.2f)) {
                                EntityHelpers.knockUp(entity, 0.7);
                            }
                        }
                        this.jumpCD = 60;
                        this.resetAnimState();
                    } else if (this.onGround() || (this.animTicks >= 60 && this.isInFluidType())) {
                        this.jumpCD = 60;
                        this.resetAnimState();
                    }
                }
                case 4 -> {
                    if (this.animTicks >= 78) {
                        this.setLeft(!this.swingingLeft());
                        this.resetAnimState();
                    }
                }
                case 5 -> {
                    if (this.animTicks >= 38) {
                        this.resetAnimState();
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        if (this.getAnimState() >= 2) {
            return false;
        }
        return super.isPushable();
    }

    /*
    private void createWave(int i) {
        WaveEntity entity = HEEntities.WAVE.get().create(this.level());
        entity.shoot(this, this.getYRot() + i);
        this.level().addFreshEntity(entity);
    }

     */

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
        if (this.isFollower() && this.distanceToSqr(this.leader) >= 50.0) {
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
                    FerocetusEntity entity = new FerocetusEntity(HBEntities.FEROCETUS.get(), this.level());
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

    @Override
    public void playIdle() {
        this.setAnimState(this.getRandom().nextInt(2) + 4);
    }

    @Override
    public boolean canPlayIdle() {
        return this.tickCount % 20 == 0 && this.getTarget() == null && this.getAnimState() == 0 && this.isInFluidType();
    }
}
