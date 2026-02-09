package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMSemiaquaticMoveControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMSwimLookControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMSwimMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.HECustomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.goal.MurkAttackGoal;
import com.hedge.hedges_expansion.entity.projectile.MurkSmoke;
import com.hedge.hedges_expansion.entity.types.HEAnimStateAnimal;
import com.hedge.hedges_expansion.entity.util.AdvancedTurningMob;
import com.hedge.hedges_expansion.entity.util.AttackHelpers;
import com.hedge.hedges_expansion.entity.util.AttackStateMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import com.hedge.hedges_expansion.registry.HEEntities;
import com.hedge.hedges_expansion.registry.HEParticles;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

public class MurkEntity extends HEAnimStateAnimal implements AttackStateMob, AdvancedTurningMob {
    private static final EntityDataAccessor<Boolean> CHARGED = SynchedEntityData.defineId(MurkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(MurkEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState powerBiteAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState sideSlamAnimationState = new AnimationState();
    public final AnimationState slideStartAnimationState = new AnimationState();
    public final AnimationState slideAnimationState = new AnimationState();
    public final AnimationState breathAnimationState = new AnimationState();

    private int attackCD = 0;
    private int roarCD = 0;
    private int projCD = 0;
    private int chargeTicks = 0;
    private int slideCount = 0;
    private int shotCount = 0;
    private int slideCD = 0;
    private int projectileRot = 0;



    public MurkEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new ATMSemiaquaticMoveControl<>(this, 40, 0.3f);
        this.lookControl = new ATMSwimLookControl<>(this, 2, 25);


        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0f);
        this.setMaxUpStep(2.0F);

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_PROJECTILE)) {
            pAmount *= 0.5f;
        }
        if (this.getAnimState() == 5) {
            pAmount *= 0.75f;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGED, false);
        this.entityData.define(LEFT, false);

    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85)
                .add(Attributes.FOLLOW_RANGE, 64F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
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
        this.goalSelector.addGoal(3, new HECustomSwimGoal(this, 1.0, 10, 8, 7, false));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, LivingEntity.class, 5));
        this.goalSelector.addGoal(1, new MurkAttackGoal(this));


        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));

    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 1.0f;
    }

    @Override
    public boolean isPushable() {
        return false;
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

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new AmphibiousPathNavigation(this, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            if (this.isCharged()) {
                Vec3 rand = EntityHelpers.getRandomVec3(1.2);
                this.level().addParticle(HEParticles.MURK_CHARGE.get(), this.getX() + rand.x + rand.x,
                        this.getY() + rand.y + 0.5, this.getZ() + rand.z, rand.x, rand.y + 0.2, rand.z);
            }

        } else {
            this.attackCD = Math.max(this.attackCD - 1, 0);
            this.projCD = Math.max(this.projCD - 1, 0);
            this.slideCD = Math.max(this.slideCD - 1, 0);
            if (this.isCharged()) {
                this.chargeTicks = Math.max(this.chargeTicks - 1, 0);
                if (this.chargeTicks == 0) {
                    this.setCharged(false);
                }
            } else {
                this.roarCD = Math.max(this.roarCD  - 1, 0);
            }
            if (this.getAnimState() > 0) {
                LivingEntity target = this.getTarget();
                this.animTicks++;
                switch (this.getAnimState()) {
                    case 1 -> {
                        if (this.animTicks == 10) {
                            List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this).scale(1.5), 1.5, 1.5, 1.5, 3);
                            for (LivingEntity entity : hit) {
                                this.doHurtTarget(entity);
                            }
                        } else if (this.animTicks >= 19) {
                            this.resetAnimState();
                        }
                    }
                    case 2 -> {
                        if (!this.isInFluidType()) {
                            this.getNavigation().stop();
                        }
                        if (this.animTicks < 8 && target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 0.5, 0));
                        }
                        else if (this.animTicks == 15) {
                            for (int i = -5; i <= 5; i+=5) {
                                MurkSmoke projectile = HEEntities.MURK_SMOKE.get().create(this.level());
                                if (projectile != null) {
                                    projectile.setCharged(this.isCharged());
                                    projectile.moveTo(this.position().add(0, 1.8, 0));
                                    projectile.shootFromRotation(this, this.getXRot(), this.getYRot() + i, 0.0f, 3, 0);
                                    this.level().addFreshEntity(projectile);
                                }
                            }
                        } else if (this.animTicks >= 28) {
                            this.shotCount--;
                            this.resetAnimState();
                            this.projCD = this.shotCount > 0 ? 3 : 200;
                        }
                    }
                    case 3 -> {
                        this.getNavigation().stop();
                        if (target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 0.5, 0));
                        }
                        if (this.animTicks == 19) {
                            EntityHelpers.aoeAttack(this, EntityHelpers.bodyAngle(this), 2, 2, 2, 2, 1.5f);
                        } else if (this.animTicks >= 39){
                            this.resetAnimState();
                        }
                    }
                    case 4 -> {
                        this.getNavigation().stop();
                        if (target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 0.5, 0));
                        }
                        if (this.animTicks == 21) {
                            this.setCharged(true);
                            this.chargeTicks = 600;
                        } else if (this.animTicks >= 65) {
                            this.resetAnimState();
                            this.roarCD = 340;
                        }
                    }
                    case 5 -> {
                        this.getNavigation().stop();
                        if (this.animTicks < 15 && target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 0.5, 0));
                        } else if (this.animTicks == 20) {
                            Vec3 v = EntityHelpers.bodyAngle(this);
                            this.setDeltaMovement(this.getDeltaMovement().add(v.scale(1.4)));
                            EntityHelpers.aoeAttack(this, v, 3, 3, 3, 1.5f, 1.5f);

                        } else if (this.animTicks >= 44) {
                            this.resetAnimState();
                        }

                    }
                    case 6 -> {
                        this.getNavigation().stop();
                        if (this.animTicks < 15 && target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 0.5, 0));
                        } else if ((this.animTicks >= 25)) {
                            this.animTicks = 0;
                            this.setAnimState(7);
                        } else if (this.animTicks >= 15 && this.animTicks % 5 == 0) {
                            Vec3 v = EntityHelpers.bodyAngle(this).scale(1.5);
                            this.setDeltaMovement(this.getDeltaMovement().add(v));
                        }
                    }
                    case 7 -> {
                        this.getNavigation().stop();
                        if (this.getAnimTicks() >= 40) {
                            this.resetAnimState();
                            this.slideCount--;
                            if (this.slideCount <= 0) {
                                this.slideCD = 200;
                            }
                        } else if (this.isInFluidType()) {
                            this.resetAnimState();
                            this.slideCount = 0;
                            this.slideCD = 200;
                        } else if (this.animTicks % 5 == 0) {
                            Vec3 v = EntityHelpers.bodyAngle(this).scale(1.3);
                            this.setDeltaMovement(this.getDeltaMovement().add(v));
                            EntityHelpers.aoeAttack(this, v, 2, 2, 2, 0.7f, 1.5f);
                        }
                    }
                    case 8 -> {
                        this.getNavigation().stop();
                        if (this.animTicks < 10 && target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 1, 0));
                        }
                        else if (this.animTicks >= 16 && this.animTicks <= 24 && this.animTicks % 2 == 0) {
                            MurkSmoke projectile = HEEntities.MURK_SMOKE.get().create(this.level());
                            if (projectile != null) {
                                projectile.setCharged(this.isCharged());
                                Vec3 v = EntityHelpers.bodyAngle(this).cross(EntityHelpers.UP).scale(this.projectileRot * 0.1);
                                projectile.moveTo(this.position().add(v.x, 0.5, v.z));
                                projectile.shootFromRotation(this, this.getXRot(), this.getYRot() + this.projectileRot, 0.0f, 3, 0);
                                this.level().addFreshEntity(projectile);
                                this.projectileRot += this.swingingLeft() ? 5 : -5;
                            }

                        } else if (this.animTicks >= 39) {
                            this.shotCount--;
                            this.resetAnimState();
                            this.projCD = 100;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD += 5;
        this.roarCD+=5;
        this.projCD+=5;
        this.slideCD+=5;
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.shootAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.powerBiteAnimationState.animateWhen(this.getAnimState() == 3, this.tickCount);

        this.roarAnimationState.animateWhen(this.getAnimState() == 4, this.tickCount);
        this.sideSlamAnimationState.animateWhen(this.getAnimState() == 5, this.tickCount);

        this.slideStartAnimationState.animateWhen(this.getAnimState() == 6, this.tickCount);
        this.slideAnimationState.animateWhen(this.getAnimState() == 7, this.tickCount);
        this.breathAnimationState.animateWhen(this.getAnimState() == 8, this.tickCount);
    }

    public boolean isCharged() {
        return this.entityData.get(CHARGED);
    }

    public void setCharged(boolean b) {
        this.entityData.set(CHARGED, b);
    }

    public boolean canRoar(double attackReach, double dist) {
        return this.roarCD == 0 && !this.isCharged() && attackReach * 20 >= dist;
    }

    public boolean canSlide(double attackReach, double dist) {
        return this.slideCD == 0 && this.onGround() && attackReach * 15 >= dist;
    }

    @Override
    public int getMaxHeadYRot() {
        return 45;
    }

    public void setSlam() {
        this.setLeft(!this.swingingLeft());
        this.setAnimState(5);
    }

    @Override
    public void setAttacking() {
        if (this.getRandom().nextInt(5) == 0) {
            this.setLeft(!this.swingingLeft());
            this.setAnimState(3);
        } else {
            this.setAnimState(1);
        }
    }

    public void setShooting() {
        if (this.isCharged()) {
            this.setLeft(!this.swingingLeft());
            this.projectileRot = this.swingingLeft() ? -10 : 10;
            this.setAnimState(8);
        } else {
            this.shotCount = this.getRandom().nextInt(3) + 1;
            this.setAnimState(2);
        }
    }

    public void setSliding() {
        if (this.isCharged()) {
            this.slideCount = 3;
        } else {
            this.slideCount = 1;
        }
        this.setAnimState(6);
    }

    public int getSlideCount() {
        return this.slideCount;
    }

    public void setSlideCount(int i) {
        this.slideCount = i;
    }

    public int getShotCount() {
        return this.shotCount;
    }

    public void setShotCount(int i) {
        this.shotCount = i;
    }

    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    public void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }


    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
    }

    public boolean canShoot(double attackReach, double dist) {
        return this.projCD == 0 && attackReach * 10 >= dist;
    }

    public int getProjCD() {
        return this.projCD;
    }

    public int getSlideCD() {
        return this.slideCD;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.0 * this.getBbWidth() * 2.0 + entity.getBbWidth();

    }

    @Override
    public boolean shouldTurnWholeBody() {
        return switch (this.getAnimState()) {
            case 2, 5 -> true;
            default -> false;
        };
    }

    @Override
    public boolean shouldLockAngle() {
        return switch(this.getAnimState()) {
            case 5, 6 -> this.getAnimTicks() > 15;
            case 7 -> true;
            default -> false;
        };
    }

    @Override
    public boolean shouldInstantTurn() {
        return switch (this.getAnimState()) {
            case 2, 4, 6, 8 -> true;
            default -> false;
        };
    }

    @Override
    public float getTurnSpeed() {
        return switch (this.getAnimState()) {
            case 1 -> 35;
            case 2, 5, 6 -> 45;
            default -> this.isInFluidType() ? 5 : 25;
        };
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }
}

