package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.blocks.HEBlocks;
import com.hedge.hedges_bestiary.client.HBSounds;
import com.hedge.hedges_bestiary.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMSemiaquaticMoveControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMSwimLookControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.MurkAttackGoal;
import com.hedge.hedges_bestiary.entity.AI.navigation.HBAmphibiousPathNavigator;
import com.hedge.hedges_bestiary.entity.projectile.MurkSmoke;
import com.hedge.hedges_bestiary.entity.types.*;
import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBParticles;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MurkEntity extends HBTamableAnimal implements AttackStateMob, AdvancedTurningMob, EggLayer {
    private static final EntityDataAccessor<Boolean> CHARGED = SynchedEntityData.defineId(MurkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(MurkEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState powerBiteAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState sideSlamAnimationState = new AnimationState();
    public final AnimationState breathAnimationState = new AnimationState();
    public final AnimationState multiBiteAnimationState = new AnimationState();

    public final SmoothAnimationState clicksAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState();

    private int attackCD = 0;
    private int powerBiteCD = 0;
    private int roarCD = 0;
    private int projCD = 0;
    private int chargeTicks = 0;
    private int shotCount = 0;
    private int projectileRot = 0;

    private float prevTrail;
    private float trail = 0.0f;


    public MurkEntity(EntityType<? extends MurkEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new ATMSemiaquaticMoveControl<>(this, 40, 0.3f);
        this.lookControl = new ATMSwimLookControl<>(this, 30);


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
    protected boolean canOwnerMount(Player player) {
        return true;
    }

    @Override
    protected boolean canOwnerCommand(Player player) {
        return player.isShiftKeyDown();
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
        this.goalSelector.addGoal(0, new HBSitWhenOrderedGoal(this, false));
        this.goalSelector.addGoal(1, new MurkAttackGoal(this));
        this.goalSelector.addGoal(2, new HBFollowOwnerGoal(this, 1.2, 1.6, 7.0f, 4.0f));
        this.goalSelector.addGoal(3, new CustomSwimGoal(this, 1.0, 10, 4, 7, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return !this.mob.isInWaterOrBubble() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !this.mob.isInWaterOrBubble() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 5));
        this.goalSelector.addGoal(6, new IdleAnimationGoal<>(this));
        this.goalSelector.addGoal(7, new DancingGoal(this));

        this.targetSelector.addGoal(0, new HBHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));

    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return this.getBbHeight()/2.45f;
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
        return new HBAmphibiousPathNavigator(this, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            if (this.isCharged()) {
                Vec3 rand = EntityHelpers.getRandomVec3(1.2);
                this.level().addParticle(HBParticles.MURK_CHARGE.get(), this.getX() + rand.x + rand.x,
                        this.getY() + rand.y + 0.5, this.getZ() + rand.z, rand.x, rand.y + 0.2, rand.z);
            }
            this.tickTrailYaw();
        } else {
            this.attackCD = Math.max(this.attackCD - 1, 0);
            this.projCD = Math.max(this.projCD - 1, 0);
            this.powerBiteCD = Math.max(this.powerBiteCD - 1, 0);
            if (this.isCharged()) {
                this.chargeTicks = Math.max(this.chargeTicks - 1, 0);
                if (this.chargeTicks == 0) {
                    this.setCharged(false);
                }
            } else {
                this.roarCD = Math.max(this.roarCD  - 1, 0);
            }
            LivingEntity target = this.getTarget();
            if (this.tickCount % 200 == 0 && target == null) {
                this.heal(10);
            }
            if (this.getAnimState() > 0) {
                this.animTicks++;
                switch (this.getAnimState()) {
                    case 1 -> {
                        if (this.animTicks == 10) {
                            List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, this.getLookAngle().scale(2.2), 1.8, 1.5, 1.8, 3);
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
                            this.lookAt(target, 30f, 30f);
                            this.getLookControl().setLookAt(target, 30f, 30f);
                        }
                        else if (this.animTicks == 15) {
                            for (int i = -5; i <= 5; i+=5) {
                                MurkSmoke projectile = HBEntities.MURK_SMOKE.get().create(this.level());
                                if (projectile != null) {
                                    projectile.setCharged(this.isCharged());
                                    projectile.moveTo(this.getEyePosition());
                                    projectile.shootFromRotation(this, Mth.clamp(this.getXRot(), -45, 45), this.getYRot() + i, 0.0f, 3, 0);
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
                        if (this.animTicks % 5 == 0 && this.animTicks < 18) {
                            if (target != null) {
                                this.lookAt(target, 15f, 30f);
                                this.getLookControl().setLookAt(target, 15f, 30f);
                            }
                            this.addDeltaMovement(EntityHelpers.bodyAngle(this).scale(this.isInFluidType() ? 0.1 : 0.25));
                        }
                        else if (this.animTicks == 19) {
                            this.powerBite();
                        } else if (this.animTicks >= 39){
                            this.resetAnimState();
                            this.powerBiteCD = 200;
                        }
                    }
                    case 4 -> {
                        this.getNavigation().stop();
                        if (this.animTicks == 23) {
                            this.setCharged(true);
                            this.chargedExplode();
                            this.chargeTicks = 600;
                        }
                        else if (this.animTicks >= 65) {
                            this.resetAnimState();
                            this.roarCD = 340;
                        }
                        if (target != null) {
                            this.getLookControl().setLookAt(target.position().add(0, 0.5, 0));
                        }
                    }
                    case 5 -> {
                        this.getNavigation().stop();
                        if (this.animTicks < 15 && target != null) {
                            this.lookAt(target, 30f, 30f);
                            this.getLookControl().setLookAt(target, 30f, 30f);

                        } else if (this.animTicks == 20) {
                            Vec3 v = EntityHelpers.bodyAngle(this);
                            this.addDeltaMovement(v.scale(0.8));
                            if (this.isCharged()) {
                                this.chargedExplode();
                            }
                            List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, v, 3, 3, 3, 8);
                            for (LivingEntity entity : hit) {
                                AttackHelpers.betterHurt(this, entity, 1.2f, 1.4f);
                            }
                        } else if (this.animTicks >= 44) {
                            this.resetAnimState();
                        }

                    }
                    case 6 -> {
                        this.getNavigation().stop();
                        if (this.animTicks < 10 && target != null) {
                            this.lookAt(target, 30f, 30f);
                            this.getLookControl().setLookAt(target, 30f, 30f);
                        }
                        else if (this.animTicks >= 16 && this.animTicks <= 24 && this.animTicks % 2 == 0) {
                            MurkSmoke projectile = HBEntities.MURK_SMOKE.get().create(this.level());
                            if (projectile != null) {
                                projectile.setCharged(this.isCharged());
                                Vec3 v = EntityHelpers.bodyAngle(this).cross(EntityHelpers.UP).scale(this.projectileRot * 0.1);
                                projectile.moveTo(this.getEyePosition().add(v));
                                projectile.shootFromRotation(this, Mth.clamp(this.getXRot(), -45, 45), this.getYRot() + this.projectileRot, 0.0f, 3, 0);
                                this.level().addFreshEntity(projectile);
                                this.projectileRot += this.swingingLeft() ? 5 : -5;
                            }

                        } else if (this.animTicks >= 39) {
                            this.shotCount--;
                            this.resetAnimState();
                            this.projCD = 100;
                        }
                    }
                    case 7 -> {
                        this.getNavigation().stop();
                        if (this.animTicks % 5 == 0 && this.animTicks < 18) {
                            if (target != null) {
                                this.lookAt(target, 15f, 30f);
                                this.getLookControl().setLookAt(target, 15f, 30f);
                            }
                            this.addDeltaMovement(EntityHelpers.bodyAngle(this).scale(this.isInFluidType() ? 0.1 : 0.25));
                        } else if (this.animTicks >= 75){
                            this.resetAnimState();
                            this.powerBiteCD = 120;
                        } else {
                            switch (this.animTicks) {
                                case 19, 36, 54 -> {
                                    this.powerBite();
                                    this.addDeltaMovement(EntityHelpers.bodyAngle(this).scale(0.4));
                                }
                            }
                        }
                    }
                    case 8 -> {
                        if (this.animTicks > 20) {
                            this.resetAnimState();
                        }
                    }
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

    private void powerBite() {
        Vec3 v = EntityHelpers.bodyAngle(this).scale(1.5);
        List<LivingEntity> hit;
        if (this.isCharged()) {
            this.level().broadcastEntityEvent(this, (byte)40);
            hit = AttackHelpers.zoneHitbox(this, v, 2, 2, 2, 8);
            for (LivingEntity entity : hit) {
                AttackHelpers.betterHurt(this, entity, 1.8f, 1.5f);
            }
        } else {
            hit = AttackHelpers.zoneHitbox(this, v, 2, 2, 2, 8);
        }
        for (LivingEntity entity : hit) {
            AttackHelpers.betterHurt(this, entity, 1.5f, 1.4f);
        }
    }

    private void spawnImpactParticle() {
        Vec3 v = this.getEyePosition().add(this.getLookAngle().scale(1.5));
        this.level().addParticle(HBParticles.MURK_IMPACT.get(),
                v.x, v.y, v.z, 0, 0, 0);
    }

    private void chargedExplode() {
        this.level().broadcastEntityEvent(this, (byte)39);
        for (int i = -180; i <= 180; i += 60) {
            MurkSmoke projectile = HBEntities.MURK_SMOKE.get().create(this.level());
            if (projectile != null) {
                projectile.setCharged(true);
                projectile.moveTo(this.position().add(0, 0.4, 0));
                projectile.shootFromRotation(this, 0, this.getYRot() + i, 0.0f, 3, 0);
                this.level().addFreshEntity(projectile);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        switch (pId) {
            case 39 -> this.level().addParticle(HBParticles.MURK_EXPLODE.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case 40 -> this.spawnImpactParticle();
            default -> super.handleEntityEvent(pId);
        }
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD += 5;
        this.roarCD+=5;
        this.projCD+=5;
        this.powerBiteCD+=5;
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.shootAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.powerBiteAnimationState.animateWhen(this.getAnimState() == 3, this.tickCount);

        this.roarAnimationState.animateWhen(this.getAnimState() == 4, this.tickCount);
        this.sideSlamAnimationState.animateWhen(this.getAnimState() == 5, this.tickCount);

        this.breathAnimationState.animateWhen(this.getAnimState() == 6, this.tickCount);
        this.multiBiteAnimationState.animateWhen(this.getAnimState() == 7, this.tickCount);

        this.clicksAnimationState.animateWhen(this.getAnimState() == 8, this.tickCount);
        this.sitAnimationState.animateWhen(this.isSitting() && !this.isDancing(), this.tickCount);
        this.danceAnimationState.animateWhen(this.isDancing(), this.tickCount);
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

    public boolean canPowerBite(double attackReach, double dist) {
        return this.powerBiteCD == 0 && attackReach * 1.4 >= dist;
    }

    public void setSlam() {
        this.setLeft(!this.swingingLeft());
        this.setAnimState(5);
    }

    @Override
    public void setAttacking() {
        this.setAnimState(1);
    }

    public void setShooting() {
        if (this.isCharged()) {
            this.setLeft(!this.swingingLeft());
            this.projectileRot = this.swingingLeft() ? -10 : 10;
            this.setAnimState(6);
        } else {
            this.shotCount = this.getRandom().nextInt(3) + 1;
            this.setAnimState(2);
        }
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


    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.0 * this.getBbWidth() * 2.0 + entity.getBbWidth();

    }

    @Override
    public boolean shouldTurnWholeBody() {
        return this.getAnimState() == 5;
    }

    @Override
    public boolean shouldLockAngle() {
        return switch(this.getAnimState()) {
            case 5, 6 -> this.animTicks > 15;
            default -> false;
        };
    }

    @Override
    public boolean shouldInstantTurn() {
        return switch (this.getAnimState()) {
            case 2, 4, 6 -> true;
            default -> false;
        };
    }

    @Override
    public float getTurnSpeed() {
        return this.isInWaterOrBubble() ? 22.25f : 45f;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }

    @Override
    public void playIdle() {
        this.setAnimState(8);
        this.playSound(HBSounds.MURK_CLICKS.get(), 1 - (this.getRandom().nextFloat() / 2), 1 - (this.getRandom().nextFloat() / 4));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.MURK.get().create(level);
    }

    @Override
    public BlockState getEgg() {
        return HEBlocks.MURK_EGG.get().defaultBlockState();
    }
}

