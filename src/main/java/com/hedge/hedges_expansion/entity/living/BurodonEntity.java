package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_expansion.entity.AI.goal.AvoidTargetWhenLowGoal;
import com.hedge.hedges_expansion.entity.AI.goal.BurodonAttackGoal;
import com.hedge.hedges_expansion.entity.AI.control.ATMLookControl;
import com.hedge.hedges_expansion.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_expansion.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_expansion.entity.types.HEAnimStateAnimal;
import com.hedge.hedges_expansion.entity.types.HETamableAnimal;
import com.hedge.hedges_expansion.entity.util.AdvancedTurningMob;
import com.hedge.hedges_expansion.entity.util.AttackStateMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BurodonEntity extends HETamableAnimal implements AttackStateMob, AdvancedTurningMob {

    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState jumpAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState yawnAnimationState = new AnimationState();


    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState airAnimationState = new AnimationState();

    private boolean jumpAway = false;
    private Vec3 jumpVector;
    public int inAirTimer = 0;
    private int attackCD = 0;
    private int jumpCD = 0;
    private int roarCD = 0;

    public static final int BITE_ANIM = 1;
    public static final int JUMP_ANIM = 2;
    public static final int ROAR_ANIM = 3;
    public static final int YAWN_ANIM = 4;

    public BurodonEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new ATMMoveControl<>(this);
        this.lookControl = new ATMLookControl<>(this);
        this.setMaxUpStep(1);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.FOLLOW_RANGE, 45F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }



    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 7));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0, 20));
        this.goalSelector.addGoal(2, new BurodonAttackGoal(this));
        this.goalSelector.addGoal(1, new AvoidTargetWhenLowGoal(this, 1.6, 20, 15, 16, 7));
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof BurodonEntity && pEntity.getTeam() == this.getTeam()) {
            return true;
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            if (this.inAir() || this.getAnimState() == JUMP_ANIM) {
                this.inAirTimer++;
            } else {
                this.inAirTimer = 0;
            }
        } else {
            if (this.tickCount % 200 == 0) {
                this.heal(10);
                if (!this.isAggressive() && this.getAnimState() == 0 && this.getRandom().nextInt(3) == 0) {
                    this.setAnimState(YAWN_ANIM);
                }
            }
            this.tickCooldowns();
            this.tickAnimState();
        }
    }

    private void tickCooldowns() {
        this.attackCD = Math.max(this.attackCD - 1, 0);
        this.jumpCD = Math.max(this.jumpCD - 1, 0);
        this.roarCD = Math.max(this.roarCD - 1, 0);
    }

    public boolean inAir() {
        return !this.isInFluidType() && !this.onGround();
    }

    private void tickAnimState() {
        if (this.getAnimState() > 0) {
            animTicks++;
            LivingEntity target = this.getTarget();
            switch (this.getAnimState()) {
                case BITE_ANIM -> {
                    if (this.animTicks == 7 && target != null && this.canHurtTarget(target)) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 15) {
                        this.attackCD = 5;
                        this.jumpAway = this.getRandom().nextBoolean();
                        this.resetAnimState();
                    }
                }
                case JUMP_ANIM-> {
                    this.navigation.stop();
                    if (this.getAnimTicks() < 15) {
                        if (target != null) {
                            if (this.jumpAway && jumpVector != null) {
                                this.getLookControl().setLookAt(jumpVector);
                            } else {
                                this.getLookControl().setLookAt(target);
                            }
                        }
                    }
                    else if (this.animTicks == 19) {
                        Vec3 v = this.getLookAngle();
                        this.setDeltaMovement(this.getDeltaMovement().add(v.x, 0.4, v.z).scale(1.5));
                    } else if (this.animTicks >= 30) {
                        this.jumpCD = 100;
                        this.jumpVector = null;
                        this.jumpAway = false;
                        this.resetAnimState();
                    }
                }
                case ROAR_ANIM -> {
                    this.navigation.stop();
                    if (target != null)
                        this.getLookControl().setLookAt(target);
                    if (this.animTicks >= 39) {
                        this.roarCD = 400;
                        this.resetAnimState();
                    }
                }
                case YAWN_ANIM -> {
                    if (this.animTicks >= 45 || target != null) {
                        this.resetAnimState();
                    }
                }
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 1.3f;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.getPose() == Pose.STANDING && keepsIdle(), this.tickCount);
        this.airAnimationState.animateWhen(inAirTimer > 5 && keepsIdle(), this.tickCount);
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.jumpAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.roarAnimationState.animateWhen(this.getAnimState() == 3, this.tickCount);
        this.yawnAnimationState.animateWhen(this.getAnimState() == 4, this.tickCount);
    }

    private boolean keepsIdle() {
        return switch (this.getAnimState()) {
            case JUMP_ANIM, ROAR_ANIM -> false;
            default -> true;
        };
    }

    @Override
    public void setAttacking() {
        this.setAnimState(BITE_ANIM);
    }

    public boolean canRoar() {
        return this.roarCD == 0 && this.random.nextInt(6) == 0;
    }

    public boolean canJump(LivingEntity entity, double attackReach, double dist) {
        if (this.jumpCD > 0)
            return false;
        if (this.getHealth() < 15) {
            this.jumpAway = true;
        }
        if (this.jumpAway) {
            jumpVector = DefaultRandomPos.getPosAway(this, 10, 7, entity.position());
            return jumpVector != null;
        }
        return attackReach * 24 >= dist && attackReach * 6 <= dist;
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.2 * this.getBbWidth() * 2.2 + entity.getBbWidth();

    }

    private boolean canHurtTarget(LivingEntity entity) {
        return this.getAttackReachSqr(entity) >= this.distanceToSqr(entity);
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

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }
}
