package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMLookControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.AvoidTargetWhenLowGoal;
import com.hedge.hedges_expansion.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_expansion.entity.types.HETamableAnimal;
import com.hedge.hedges_expansion.entity.types.AdvancedTurningMob;
import com.hedge.hedges_expansion.entity.types.AttackStateMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GruinEntity extends HETamableAnimal implements AttackStateMob, AdvancedTurningMob {

    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(GruinEntity.class, EntityDataSerializers.BOOLEAN);


    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState swipeAnimationState = new AnimationState();
    public final AnimationState sniffAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();

    public final AnimationState idleAnimationState = new AnimationState();
    private int attackCD = 0;

    public GruinEntity(EntityType<? extends GruinEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new ATMLookControl<>(this);
        this.moveControl = new ATMMoveControl<>(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.89)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.22F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 8));
        this.goalSelector.addGoal(2, new GenericMeleeGoal<>(this, 1.5) {
            @Override
            protected double getSpeedModifier() {
                return switch (this.mob.getAnimState()) {
                    case 1, 2 -> 1;
                    default -> super.getSpeedModifier();
                };
            }
        });
        this.goalSelector.addGoal(1, new AvoidTargetWhenLowGoal(this, 1.6f, 20, 30, 20, 3));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.yBodyRot = Mth.approachDegrees(yBodyRotO, this.yBodyRot, 10);
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
        } else {
            if (this.tickCount % 200 == 0) {
                this.heal(20);
                if (!this.isAggressive() && this.getAnimState() == 0 && this.getRandom().nextInt(3) == 0) {
                    this.setAnimState(4);
                }
            }
            this.attackCD = Math.max(this.attackCD - 1, 0);
            this.tickAnimState();
        }
    }

    private void tickAnimState() {
        if (this.getAnimState() > 0) {
            animTicks++;
            LivingEntity target = this.getTarget();
            switch (this.getAnimState()) {
                case 1 -> {
                    if (this.animTicks == 12 && target != null && this.canHurtTarget(target)) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 17) {
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    if (this.animTicks == 13 && target != null && this.canHurtTarget(target)) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 21) {
                        this.resetAnimState();
                    }
                }
                case 4 -> {
                    if (this.getTarget() != null || this.getAnimTicks() >= 49) {
                        this.resetAnimState();
                    }
                }

            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public void setUpAnimStates() {
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.swipeAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.roarAnimationState.animateWhen(this.getAnimState() == 3, this.tickCount);
        this.sniffAnimationState.animateWhen(this.getAnimState() == 4, this.tickCount);

        this.idleAnimationState.animateWhen(this.getPose() == Pose.STANDING, this.tickCount);

    }



    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD = 5;
    }

    @Override
    public boolean shouldTurnWholeBody() {
        return this.getAnimState() == 2;
    }

    @Override
    public boolean shouldLockAngle() {
        return false;
    }

    @Override
    public boolean shouldInstantTurn() {
        return false;
    }

    @Override
    public int getMaxHeadXRot() {
        return 25;
    }

    @Override
    public int getMaxHeadYRot() {
        return 25;
    }

    @Override
    public float getTurnSpeed() {
        return 25;
    }

    @Override
    public void setAttacking() {
        int i = this.getRandom().nextInt(2) + 1;
        if (i == 2) {
            this.setLeft(!this.swingingLeft());
        }
        this.setAnimState(i);
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && this.getAnimState() == 0 && this.canHurtTarget(entity);
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 1.8 * this.getBbWidth() * 1.8 + entity.getBbWidth();
    }

    private boolean canHurtTarget(LivingEntity entity) {
        return this.getAttackReachSqr(entity) >= this.distanceToSqr(entity);
    }

    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    public void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }

}
