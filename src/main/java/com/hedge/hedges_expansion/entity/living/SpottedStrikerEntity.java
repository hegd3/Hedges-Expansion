package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_expansion.entity.AI.control.HESwimmingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.HECustomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.FluidPathNavigation;
import com.hedge.hedges_expansion.entity.types.HEAquaticMob;
import com.hedge.hedges_expansion.entity.types.HEBucketableSchoolingMob;
import com.hedge.hedges_expansion.entity.types.HESchoolingMob;
import com.hedge.hedges_expansion.entity.util.AttackHelpers;
import com.hedge.hedges_expansion.entity.types.AttackStateMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpottedStrikerEntity extends HEAquaticMob implements AttackStateMob {

    private int attackCD = 0;
    private int nextAttack = 1;
    private float prevTrail;
    private float trail = 0.0f;

    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState superBiteAnimationState = new AnimationState();

    public SpottedStrikerEntity(EntityType<? extends SpottedStrikerEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new HESwimmingMoveControl(this, 999, 5, 0.02f, 0.0f);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }


    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4)
                .add(Attributes.FOLLOW_RANGE, 25F)
                .add(Attributes.MOVEMENT_SPEED, 0.8F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new HECustomSwimGoal(this, 1.0f, 30, 4, 5, false));
        this.goalSelector.addGoal(1, new GenericMeleeGoal<>(this, 1.2f) {
            @Override
            protected double getSpeedModifier() {
                if (this.mob.getAnimState() > 0) return 0.7;
                return super.getSpeedModifier();
            }
        });
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, HESchoolingMob.class, true));

    }


    @Override
    public void aiStep() {
        this.flop();
        super.aiStep();
    }

    @Override
    public void serverTick() {
        this.attackCD = Math.max(attackCD - 1, 0);
        int animState = this.getAnimState();
        if (animState > 0) {
            animTicks++;
            LivingEntity target = this.getTarget();
            switch (animState) {
                case 1 -> {
                    if (this.animTicks == 8 && target != null) {
                        if (AttackHelpers.singleTargetHitbox(this, target, this.getLookAngle(), 1, 1, 1)) {
                            this.doHurtTarget(target);
                        }
                    } else if (this.animTicks >= 14) {
                        this.attackCD = 5;
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    if (this.animTicks == 22) {
                        Vec3 v = EntityHelpers.bodyAngle(this);
                        this.addDeltaMovement(v.scale(0.6));
                        List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, v, 2, 2, 2, 5);
                        for (LivingEntity entity : hit) {
                            if (!AttackHelpers.blockBreak(this, entity)) {
                                AttackHelpers.betterHurt(this, entity, 2f, 1.4f);
                            }
                        }
                    }  else if (this.animTicks >= 29) {
                        this.attackCD = 5;
                        this.resetAnimState();
                    }
                }
            }
        }
    }

    @Override
    protected void clientTick() {
        super.clientTick();
        this.tickTrailYaw();
    }

    private void tickTrailYaw() {
        this.prevTrail = this.trail;
        this.trail += (-(this.yBodyRot - this.yBodyRotO) - this.trail) * 0.15F;
    }

    public float getTrailYaw(float partialTick) {
        return (this.prevTrail + (this.trail - this.prevTrail) * partialTick);
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.superBiteAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);

    }

    private boolean canSuperBite(double attackReach, double dist) {
        return this.tickCount % 5 == 0 && attackReach * 5 >= dist && this.getRandom().nextInt(10) == 0;
    }

    @Override
    public void setAttacking() {
        this.setAnimState(this.nextAttack);
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        if (this.attackCD > 0)
            return false;
        if (this.getAnimState() == 0) {
            if (this.canSuperBite(attackReach, dist)) {
                this.nextAttack = 2;
                return true;
            }
            this.nextAttack = 1;
            return attackReach >= dist;
        }
        return false;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.2 * this.getBbWidth() * 2.2 + entity.getBbWidth();
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FluidPathNavigation(this, pLevel);
    }
}
