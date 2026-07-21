package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.entity.AI.control.FlyingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.FlyingWanderGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.HBHurtByTargetGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.BansheeAttackGoal;
import com.hedge.hedges_bestiary.entity.projectile.BansheeScream;
import com.hedge.hedges_bestiary.entity.types.HBMonster;
import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BansheeEntity extends HBMonster {
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(BansheeEntity.class, EntityDataSerializers.BOOLEAN);


    public float roll = 0.0f;
    private float prevTrail;
    private float trail = 0.0f;
    private int spinCD = 0;
    private int screamCD = 0;
    private int diveCD = 0;


    public final AnimationState spinAnimationState = new AnimationState();
    public final SmoothAnimationState windupAnimationState = new SmoothAnimationState(0.3f);
    public final SmoothAnimationState screamAnimationState = new SmoothAnimationState(0.3f);
    public final SmoothAnimationState diveAnimationState = new SmoothAnimationState(0.15f);

    public BansheeEntity(EntityType<? extends BansheeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 999, 10, 1.0f);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BansheeAttackGoal(this));
        this.goalSelector.addGoal(1, new FlyingWanderGoal(this, 1.0f, 35, 22));

        this.targetSelector.addGoal(0, new HBHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);
    }



    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FlyingPathNavigation(this, pLevel);
    }

    @Override
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
    protected void serverTick() {
        if (this.getAnimState() > 0) {
            this.animTicks++;
            switch (this.getAnimState()) {
                case 1 -> {
                    if (this.animTicks >= 36) {
                        this.resetAnimState();
                        this.setLeft(this.getRandom().nextBoolean());
                        this.spinCD = 100 + this.getRandom().nextInt(60);
                    }
                } case 2 -> {
                    LivingEntity target = this.getTarget();
                    if (target != null) {
                        this.lookAt(target, 15f, 30f);
                        this.getLookControl().setLookAt(target, 15f, 30f);
                    }

                    if (this.animTicks >= 30) {
                        if (target != null) {
                            this.animTicks = 0;
                            this.setAnimState(3);
                        } else {
                            this.resetAnimState();
                        }
                    }
                } case 3 -> {
                    LivingEntity target = this.getTarget();
                    if (target == null) {
                        this.resetAnimState();
                        this.screamCD = 100;
                    } else if (this.animTicks >= 60) {
                        if (this.canDive(target)) {
                            this.animTicks = 0;
                            this.setAnimState(4);
                        } else {
                            this.resetAnimState();
                        }
                        this.screamCD = 100;
                    } else {
                        this.lookAt(target, 10f, 30f);
                        this.getLookControl().setLookAt(target, 10f, 30f);
                        if (this.animTicks % 5 == 0) {
                            BansheeScream projectile = HBEntities.BANSHEE_SCREAM.get().create(this.level());
                            projectile.moveTo(this.getEyePosition());
                            projectile.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0f, 3, 0);
                            projectile.setXRot(this.getXRot());
                            projectile.setYRot(this.getYRot());
                            this.level().addFreshEntity(projectile);

                        }
                    }
                } case 4 -> {
                    LivingEntity target = this.getTarget();

                    if (this.animTicks >= 5) {
                        if (this.verticalCollision || this.horizontalCollision || this.verticalCollisionBelow || target == null || this.animTicks >= 60) {
                            this.animTicks = 0;
                            this.setAnimState(1);
                            this.diveCD = 100;
                        } else if (this.animTicks % 5 == 0)  {
                            this.lookAt(target, 2.5f, 15f);
                            this.getLookControl().setLookAt(target.getX(), target.getY() - 1, target.getZ(), 2.5f, 15f);
                            this.setDeltaMovement(EntityHelpers.bodyAngle(this, this.getXRot()).scale(0.04 * this.animTicks));
                            List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, Vec3.ZERO, 3, 3, 3, 5);
                            for (LivingEntity entity : hit) {
                                this.doHurtTarget(entity);
                            }
                        }

                    } else {
                        if (target != null) {
                            this.lookAt(target, 10f, 30f);
                            this.getLookControl().setLookAt(target, 10f, 30f);
                        }
                    }
                }
            }
        }

        this.spinCD = Math.max(this.spinCD - 1, 0);
        this.screamCD = Math.max(this.screamCD - 1, 0);
        this.diveCD = Math.max(this.diveCD - 1, 0);
    }

    @Override
    public void setUpAnimStates() {
        int state = this.getAnimState();
        this.idleAnimationState.animateWhen(this.isAlive() && state != 4, this.tickCount);
        this.spinAnimationState.animateWhen(state == 1, this.tickCount);
        this.windupAnimationState.animateWhen(state == 2, this.tickCount);
        this.screamAnimationState.animateWhen(state == 3, this.tickCount);
        this.diveAnimationState.animateWhen(state == 4, this.tickCount);
    }

    @Override
    public boolean isPushable() {
        if (this.getAnimState() == 4) {
            return false;
        }
        return super.isPushable();
    }

    @Override
    protected void clientTick() {
        super.clientTick();
        this.tickTrailYaw();
        this.tickRoll();
    }

    private void tickTrailYaw() {
        this.prevTrail = this.trail;
        this.trail += (-(this.yBodyRot - this.yBodyRotO) - this.trail) * 0.15F;
    }

    public float getTrailYaw(float partialTick) {
        return (this.prevTrail + (this.trail - this.prevTrail) * partialTick);
    }


    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public boolean getLeft() {
        return this.entityData.get(LEFT);
    }

    public void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }

    public boolean canSpin() {
        return this.spinCD == 0;
    }

    public boolean canDive(double attackReach, double dist, double yDifference) {
        return this.diveCD == 0 && attackReach * 1.5 >= dist && yDifference > 6;
    }

    public boolean canDive(LivingEntity target) {
        return this.diveCD == 0 && this.getAttackReachSqr(target) * 1.5 >= this.distanceToSqr(target) && this.getY() - target.getY() > 6;
    }

    @Override
    public void setAttacking() {
        this.getNavigation().stop();
        this.setAnimState(2);
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return attackReach >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 15 * this.getBbWidth() * 15 + entity.getBbWidth();
    }

    private void tickRoll() {
        float prevRoll = this.roll;
        float targetRoll = Math.max(-0.45F, Math.min(0.45F, (this.getYRot() - this.yRotO) * 0.1F));
        targetRoll = -targetRoll;
        this.roll = prevRoll + (targetRoll - prevRoll) * 0.05F;
    }

}
