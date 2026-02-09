package com.hedge.hedges_expansion.entity.living;


import com.hedge.hedges_expansion.client.particle.SmokeParticleOptions;
import com.hedge.hedges_expansion.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_expansion.entity.AI.goal.BehemothAttackGoal;
import com.hedge.hedges_expansion.entity.AI.control.ATMLookControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_expansion.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_expansion.entity.types.HEMonster;
import com.hedge.hedges_expansion.entity.util.AdvancedTurningMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BehemothEntity extends HEMonster implements AdvancedTurningMob {
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(BehemothEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(BehemothEntity.class, EntityDataSerializers.INT);

    public final AnimationState hornAttackAnimationState = new AnimationState();
    public final AnimationState armAtackAnimationState = new AnimationState();
    public final AnimationState bodySlamAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState chargeStartUpAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();
    public final AnimationState jumpAnimationState = new AnimationState();



    public final AnimationState headTwitchAnimationState = new AnimationState();
    public final AnimationState landAnimationState = new AnimationState();
    public final AnimationState airAnimationState = new AnimationState();

    private int attackCD = 0;
    private int chargeCD = 0;
    private int jumpCD = 0;
    private int roarCD = 0;
    private int airTime = 0;

    public static final int BITE_ANIM = 1;
    public static final int HORN_ANIM = 2;
    public static final int ARM_SLAM_ANIM = 3;
    public static final int BODY_SLAM_ANIM = 4;
    public static final int ROAR_ANIM = 5;
    public static final int CHARGE_STARTUP_ANIM = 6;
    public static final int CHARGE_ANIM = 7;
    public static final int JUMP_ANIM = 8;
    public static final int LAND_ANIM = 9;




    public BehemothEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new ATMLookControl<>(this);
        this.moveControl = new ATMMoveControl<>(this);
        this.setMaxUpStep(2);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ARMOR, 12D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.98)
                .add(Attributes.FOLLOW_RANGE, 45F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 7));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0, 20));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BehemothAttackGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));

    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimState() == 8 || !this.onGround() && !this.isInFluidType()) {
            airTime++;
        } else {
            airTime = 0;
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey == ANIM_STATE && this.level().isClientSide()) {
            if (this.getAnimState() == 0) {
                this.animTicks = 0;
            }
        }
    }

    @Override
    protected void clientTick() {
        super.clientTick();
        if (this.getAnimState() > 0) {
            this.animTicks++;
            switch (this.getAnimState()) {
                case CHARGE_STARTUP_ANIM, CHARGE_ANIM -> {
                    if (this.animTicks % 10 == 0) {
                        Vec3 pos = this.position();
                        for (int i = 0; i < Math.min(this.animTicks / 5, 5); i++) {
                            Vec3 pV = EntityHelpers.getRandomVec3(2);
                            this.level().addParticle(new SmokeParticleOptions(1f, 20, 0xAB9893),
                                    pos.x + pV.x, pos.y, pos.z + pV.z, 0, 0.1, 0.0);
                        }
                    }
                }
                case LAND_ANIM -> {
                    if (this.animTicks % 5 == 0) {
                        Vec3 pos = this.position();
                        for (int i = 0; i < 6; i++) {
                            Vec3 pV = EntityHelpers.getRandomVec3(2);
                            this.level().addParticle(new SmokeParticleOptions(1f, 10, 0xAB9893),
                                    pos.x + pV.x, pos.y, pos.z + pV.z, pV.x * 0.1, 0.4, pV.z * 0.1);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void serverTick() {
        this.tickCooldowns();
        int animState = this.getAnimState();
        if (this.tickCount % 200 == 0) {
            this.heal(10);
        }
        if (animState > 0) {
            this.animTicks++;
            LivingEntity target = this.getTarget();
            switch (animState) {
                case BITE_ANIM -> {
                    if (this.animTicks == 18) {
                        Vec3 v = EntityHelpers.bodyAngle(this).scale(1.3);
                        EntityHelpers.aoeAttack(this, v,2, 2, 2, 1.0f, 2.0f);
                    } else if (this.animTicks >= 34) {
                        this.resetAnimState();
                    }
                }
                case HORN_ANIM -> {
                    if (this.animTicks == 21) {
                        Vec3 v = EntityHelpers.bodyAngle(this).scale(1.3);
                        this.setDeltaMovement(this.getDeltaMovement().add(v));
                        List<LivingEntity> targets = EntityHelpers.aoeAttack(this, v,2, 4, 2, 0.7f, 2.0f);

                        for (LivingEntity hit : targets) {
                            EntityHelpers.knockUp(hit, 0.7);
                        }

                    } else if (this.animTicks >= 35) {
                        this.resetAnimState();
                    }
                }
                case ARM_SLAM_ANIM -> {
                    this.navigation.stop();
                    if (this.animTicks < 19 && target != null) {
                        this.getLookControl().setLookAt(target);
                    }
                    else if (this.animTicks == 26) {
                        Vec3 v = EntityHelpers.bodyAngle(this).scale(1.7);
                        List<LivingEntity> targets = EntityHelpers.aoeAttack(this, v, this.swingingLeft() ? -1.8 : 1.8, 2, 2, 2, 1.5f, 1.4f);

                        for (LivingEntity hit : targets) {
                            EntityHelpers.knockUp(hit, 1.0);
                        }

                    } else if (this.animTicks >= 60) {
                        this.resetAnimState();
                    }
                }
                case BODY_SLAM_ANIM -> {
                    this.navigation.stop();
                    if (animTicks < 24 && target != null) {
                        this.getLookControl().setLookAt(target);
                    }
                    else if (this.animTicks == 30) {
                        Vec3 v = EntityHelpers.bodyAngle(this).scale(1.7);
                        List<LivingEntity> targets = EntityHelpers.aoeAttack(this, v, 4, 3, 4, 2.4f, 1.4f);

                        for (LivingEntity hit : targets) {
                            EntityHelpers.knockUp(hit, 1.2);
                        }
                    } else if (this.animTicks >= 68) {
                        this.resetAnimState();
                    }

                }
                case ROAR_ANIM -> {
                    if (this.animTicks > 60) {
                        this.resetAnimState();
                        this.roarCD = 500;
                    }
                }
                case CHARGE_STARTUP_ANIM -> {
                    this.navigation.stop();
                    if (this.animTicks < 24 && target != null) {
                        this.getLookControl().setLookAt(target);
                    }
                    else if (this.animTicks > 30) {
                        this.animTicks = 0;
                        this.setAnimState(CHARGE_ANIM);
                    }
                }
                case CHARGE_ANIM -> {
                    this.navigation.stop();
                    if (this.animTicks > 40) {
                        this.resetAnimState();
                        this.chargeCD = 300;
                    }
                    else if (this.animTicks % 5 == 0) {
                        Vec3 v = EntityHelpers.bodyAngle(this).scale(1.5);
                        this.setDeltaMovement(this.getDeltaMovement().add(v));
                        EntityHelpers.aoeAttack(this, v, 3, 3, 3, 1.2f, 4);
                    }
                }
                case JUMP_ANIM -> {
                    this.navigation.stop();
                    if (this.animTicks < 24 && target != null) {
                        this.getLookControl().setLookAt(target);
                    }
                    else if (this.animTicks == 30) {
                        Vec3 v = EntityHelpers.bodyAngle(this);
                        double sc;
                        if (target != null) {
                            sc = Math.min(3, Math.max(1.2, this.distanceToSqr(target) * 0.1));
                        } else {
                            sc = 1.2;
                        }
                        this.setDeltaMovement(this.getDeltaMovement().add(v.x, 0, v.z).scale(sc).add(0 , 1.6, 0));
                    } else if (this.animTicks > 35) {
                        this.resetAnimState();
                    }
                }
                case LAND_ANIM -> {
                    this.navigation.stop();
                    if (this.animTicks == 5) {
                        List<LivingEntity> targets = EntityHelpers.aoeAttack(this, Vec3.ZERO, 6, 6, 6, 2.4f, 1.4f);

                        for (LivingEntity hit : targets) {
                            EntityHelpers.knockUp(hit, 1.5);
                        }
                    } else if (this.animTicks > 28) {
                        this.resetAnimState();
                        this.jumpCD = 200;
                    }

                }

            }
        }
    }



    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        if (pFallDistance > 2.5f) {
            this.setAnimState(9);
        }
        return 0;
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD = 5;
        this.roarCD+=5;
        this.chargeCD+=5;
        this.jumpCD+=5;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_PROJECTILE))
            pAmount *= 0.5F;
        return super.hurt(pSource, pAmount);
    }

    private void tickCooldowns() {
        this.attackCD = Math.max(this.attackCD - 1, 0);
        this.chargeCD = Math.max(this.chargeCD - 1, 0);
        this.roarCD = Math.max(this.roarCD - 1, 0);
        if (this.airTime == 0)
            this.jumpCD = Math.max(this.jumpCD - 1, 0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 3.7f;
    }


    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {

        this.setVariant(this.getRandom().nextInt(4));

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public boolean canCharge(double attackReach, double dist) {
        if (this.chargeCD > 0 || !this.onGround())
            return false;
        return attackReach * 44 >= dist;

    }

    public boolean canJump(double attackReach, double dist) {
        if (this.jumpCD > 0 || !this.onGround())
            return false;

        if (attackReach * 24 >= dist) {
            this.jumpCD = 200;
            return true;
        }
        return false;
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        if (this.attackCD > 0 || !this.onGround() && !this.isInFluidType())
            return false;
        this.setLeft(!EntityHelpers.rightOfEntity(this, entity));
        return super.canUseAttack(entity, attackReach, dist);
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.hornAttackAnimationState.animateWhen(this.getAnimState() == HORN_ANIM, this.tickCount);
        this.armAtackAnimationState.animateWhen(this.getAnimState() == ARM_SLAM_ANIM, this.tickCount);
        this.bodySlamAnimationState.animateWhen(this.getAnimState() == BODY_SLAM_ANIM, this.tickCount);
        this.roarAnimationState.animateWhen(this.getAnimState() == ROAR_ANIM, this.tickCount);
        this.chargeStartUpAnimationState.animateWhen(this.getAnimState() == CHARGE_STARTUP_ANIM, this.tickCount);
        this.chargeAnimationState.animateWhen(this.getAnimState() == CHARGE_ANIM, this.tickCount);
        this.jumpAnimationState.animateWhen(this.getAnimState() == JUMP_ANIM, this.tickCount);


        this.headTwitchAnimationState.animateWhen(this.getAnimState() == 0, this.tickCount);
        this.landAnimationState.animateWhen(this.getAnimState() == LAND_ANIM, this.tickCount);
        this.airAnimationState.animateWhen(this.airTime > 10 && this.playsIdle(), this.tickCount);
    }

    public boolean canRoar() {
        return this.roarCD == 0 && this.random.nextInt(10) == 0;
    }


    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    public void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }

    public void setVariant(int i) {
        this.entityData.set(VARIANT, i);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }



    @Override
    public void setAttacking() {
        this.setAnimState(this.getRandom().nextInt(4) + 1);
    }

    @Override
    public boolean shouldTurnWholeBody() {
        return switch (this.getAnimState()) {
            case BITE_ANIM, HORN_ANIM -> this.animTicks < 10;
            case ARM_SLAM_ANIM, BODY_SLAM_ANIM -> true;
            default -> false;
        };
    }

    @Override
    public boolean shouldLockAngle() {
        return switch (this.getAnimState()) {
            case ARM_SLAM_ANIM -> this.animTicks > 20;
            case BODY_SLAM_ANIM, CHARGE_STARTUP_ANIM, JUMP_ANIM -> this.animTicks > 25;
            case CHARGE_ANIM, LAND_ANIM -> true;
            default -> false;
        };
    }

    @Override
    public int getMaxHeadYRot() {
        if (this.shouldTurnWholeBody()) {
            return 60;
        }
        return 20;
    }



    @Override
    public boolean shouldInstantTurn() {
        return switch (this.getAnimState()) {
            case JUMP_ANIM, CHARGE_STARTUP_ANIM -> true;
            default -> false;
        };
    }

    @Override
    public float getTurnSpeed() {
        if (this.shouldTurnWholeBody()) {
            return 60;
        }
        return 20;
    }

    @Override
    public boolean keepsIdle() {
        if (this.airTime > 10)
            return false;
        return this.playsIdle();
    }

    public boolean playsIdle() {
        return switch (this.getAnimState()) {
            case ARM_SLAM_ANIM, BODY_SLAM_ANIM, CHARGE_STARTUP_ANIM, CHARGE_ANIM, JUMP_ANIM, LAND_ANIM -> false;
            default -> true;
        };
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }
}
