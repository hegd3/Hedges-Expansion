package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.entity.AI.control.FlyingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.FlyingWanderGoal;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.EndgelAttackGoal;
import com.hedge.hedges_bestiary.entity.AI.targeting.HBHurtByTargetGoal;
import com.hedge.hedges_bestiary.entity.projectile.EndgelBullet;
import com.hedge.hedges_bestiary.entity.types.HBMonster;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public class EndgelEntity extends HBMonster {
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(EndgelEntity.class, EntityDataSerializers.BOOLEAN);


    public float roll = 0.0f;
    private float prevTrail;
    private float trail = 0.0f;
    private int spinCD = 0;
    private int attackCD = 0;
    private int projAngle = 0;

    public final AnimationState spinAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    public EndgelEntity(EntityType<? extends EndgelEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 999, 6, 1.0f);
        this.lookControl = new SmoothSwimmingLookControl(this, 6);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.FOLLOW_RANGE, 70F)
                .add(Attributes.MOVEMENT_SPEED, 0.18F);
    }

    public int getMaxHeadXRot() {
        return 1;
    }

    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EndgelAttackGoal(this));
        this.goalSelector.addGoal(1, new FlyingWanderGoal(this, 1.0f, 35, 25));

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
                    if (this.animTicks == 25) {
                        for (int i = -45; i < 45; i+=45) {
                            for (int j = -25; j < 25; j+=25) {
                                EndgelBullet scream = new EndgelBullet(HBEntities.ENDGEL_BULLET.get(), this.level());
                                scream.moveTo(this.position());
                                scream.shootFromRotation(this, this.getXRot() + j, this.getYRot() + i, 0.0f, 2, 0);
                                scream.setTarget(this.getTarget());
                                level().addFreshEntity(scream);
                            }
                        }
                    } else if (this.animTicks > 34) {
                        this.resetAnimState();
                        this.attackCD = 200;
                    }
                }
                case 2 -> {
                    if (this.animTicks > 69) {
                        this.resetAnimState();
                        this.setLeft(this.getRandom().nextBoolean());
                        this.spinCD = 100 + this.getRandom().nextInt(60);
                    } else if (this.animTicks < 50 && this.animTicks % 5 == 0) {
                        EndgelBullet scream = new EndgelBullet(HBEntities.ENDGEL_BULLET.get(), this.level());
                        Vec3 v = new Vec3(Mth.sin(projAngle * Mth.DEG_TO_RAD) * 2, Mth.cos(projAngle * Mth.DEG_TO_RAD) * 2, 0);

                        v = v.yRot(-(this.getYRot() + (this.getLeft() ? -90 : 90)) * Mth.DEG_TO_RAD);

                        scream.moveTo(this.position().add(v));
                        scream.shootFromRotation(this, this.projAngle, this.getLeft() ? -90 + this.getYRot() : 90 + this.getYRot(), 0.0f, 2, 0);
                        scream.setTarget(this.getTarget());
                        level().addFreshEntity(scream);
                        this.projAngle+= 36;
                    }
                }
            }
        }
        this.attackCD = Math.max(this.attackCD - 1, 0);
        this.spinCD = Math.max(this.spinCD - 1, 0);
    }



    @Override
    public void setUpAnimStates() {
        int state = this.getAnimState();
        this.idleAnimationState.animateWhen(true, this.tickCount);
        this.shootAnimationState.animateWhen(state == 1, this.tickCount);
        this.spinAnimationState.animateWhen(state == 2, this.tickCount);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void clientTick() {
        super.clientTick();
        this.tickTrailYaw();
        this.tickRoll();
        for (int i =0; i < 2; i++) {
        Vec3 rand = EntityHelpers.getRandomVec3(this.getRandom(), 1.5);
        this.level().addParticle(HBParticles.ENDGEL_TRAIL.get(), this.getX() + rand.x + rand.x,
                this.getY() + rand.y + 1, this.getZ() + rand.z, rand.x, rand.y + 0.2, rand.z);
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

    public void setSpin() {
        this.setAnimState(2);
        this.projAngle = -180;
    }


    @Override
    public void setAttacking() {
        this.setAnimState(1);
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
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
