package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMLookControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_expansion.entity.AI.goal.SkartleAttackGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_expansion.entity.projectile.CorrosiveSpit;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;


public class SkartleEntity extends HEAnimStateAnimal implements AttackStateMob, AdvancedTurningMob {
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(SkartleEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState clawAnimationState = new AnimationState();
    public final AnimationState spitAnimationState = new AnimationState();

    private int attackCD = 0;
    private int spitCD = 0;

    public SkartleEntity(EntityType<? extends SkartleEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new ATMLookControl<>(this);
        this.moveControl = new ATMMoveControl<>(this);
        this.setMaxUpStep(2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SkartleAttackGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, LivingEntity.class, 8));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);

    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
        } else {
            this.spitCD = Math.max(this.spitCD - 1, 0);
            this.attackCD = Math.max(this.attackCD - 1, 0);
            if (this.getAnimState() > 0) {
                animTicks++;
                LivingEntity target = this.getTarget();
                switch (this.getAnimState()) {
                    case 1 -> {
                        if (this.animTicks == 10 && target != null) {
                            if (AttackHelpers.singleTargetHitbox(this, target, this.getLookAngle(), 1, 2, 2)) {
                                if (this.doHurtTarget(target)) {
                                    ((ServerLevel) this.level()).sendParticles(HEParticles.CORROSIVE_SPIT.get(),
                                            target.getX(), target.getY(), target.getZ(), 1, 0, 0, 0, 0.1);
                                }
                            }

                        }
                        else if (this.animTicks >= 14) {
                            this.resetAnimState();
                        }
                    }
                    case 2 -> {
                        if (this.animTicks == 9) {
                            this.setDeltaMovement(EntityHelpers.bodyAngle(this).scale(1.3));
                        }
                        else if (this.animTicks == 12 && target != null) {
                            if (AttackHelpers.singleTargetHitbox(this, target, this.getLookAngle(), 2, 2, 2)) {
                                AttackHelpers.betterHurt(this, target, 1.2f);
                            }

                        }
                        else if (this.animTicks >= 17) {
                            this.resetAnimState();
                        }
                    }
                    case 3 -> {
                        if (this.animTicks < 12 && target != null) {
                            this.getLookControl().setLookAt(target);
                        } else if (this.animTicks == 12) {
                            CorrosiveSpit spit = HEEntities.CORROSIVE_SPIT.get().create(this.level());
                            if (spit != null) {
                                spit.moveTo(this.position().add(0, 1.8, 0));
                                spit.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0f, 3, 0);
                                this.level().addFreshEntity(spit);

                            }
                        } else if (this.animTicks >= 19) {
                            this.resetAnimState();
                            this.spitCD = 200;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        if (this.getAnimState() == 2) {
            return false;
        }
        return super.isPushable();
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.clawAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.spitAnimationState.animateWhen(this.getAnimState() == 3, this.tickCount);

    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD = 2;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    @Override
    public void setAttacking() {
        if (this.getRandom().nextInt(3) == 0) {
            this.setAnimState(1);
        } else {
            this.setLeft(!this.swingingLeft());
            this.setAnimState(2);
        }
    }

    public boolean canSpit(double attackReach, double dist) {
        return this.getAnimState() == 0 && this.spitCD == 0 && attackReach * 10 >= dist;
    }

    public int getSpitCD() {
        return this.spitCD;
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach * 1.2 >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 1.8 * this.getBbWidth() * 1.8 + entity.getBbWidth();
    }

    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    private void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }

    @Override
    public boolean shouldTurnWholeBody() {
        return switch (this.getAnimState()) {
            case 2, 3 -> true;
            default -> false;
        };
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
    public float getTurnSpeed() {
        return 90;
    }
}
