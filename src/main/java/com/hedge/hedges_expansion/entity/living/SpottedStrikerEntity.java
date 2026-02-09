package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_expansion.entity.AI.control.HESwimmingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.HECustomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.FluidPathNavigation;
import com.hedge.hedges_expansion.entity.types.HEAquaticMob;
import com.hedge.hedges_expansion.entity.util.AttackHelpers;
import com.hedge.hedges_expansion.entity.util.AttackStateMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;

public class SpottedStrikerEntity extends HEAquaticMob implements AttackStateMob {

    private int attackCD = 0;
    private int nextAttack = 1;
    public float tilt = 0.0f;
    public SpottedStrikerEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new HESwimmingMoveControl(this, 40, 5, 0.02f, 0.1f);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }


    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4)
                .add(Attributes.FOLLOW_RANGE, 25F)
                .add(Attributes.MOVEMENT_SPEED, 0.9F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new HECustomSwimGoal(this, 1.0f, 10, 20, 10, true));
        this.goalSelector.addGoal(1, new GenericMeleeGoal<>(this, 1.2f));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));

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
                    if (this.animTicks == 9 && target != null) {
                        if (AttackHelpers.singleTargetHitbox(this, target, this.getLookAngle(), 1, 1, 1)) {
                            this.doHurtTarget(target);
                        }
                    } else if (this.animTicks >= 10) {
                        this.attackCD = 5;
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    if (this.animTicks == 25) {
                        this.setDeltaMovement(this.getDeltaMovement().add(this.getLookAngle().scale(0.6)));
                        EntityHelpers.aoeAttack(this, this.getLookAngle().scale(1.5), 2, 2, 2, 2.5f, 2);
                    }  else if (this.animTicks >= 33) {
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
        if (this.isInFluidType()) {
            final float v = Mth.degreesDifference(this.getYRot(), yRotO);
            if (Math.abs(v) > 1) {
                if (Math.abs(tilt) < 25)
                {
                    tilt -= Math.signum(v);
                }
            } else {
                if (Math.abs(tilt) > 0)
                { final float tiltSign = Math.signum(tilt);
                    tilt -= tiltSign * 0.85F;
                    if (tilt * tiltSign < 0)
                    { tilt = 0; }
                }
            }
        }
        else {
            tilt = 0;
        }
    }

    private boolean canMegaBite(double attackReach, double dist) {
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
            if (this.canMegaBite(attackReach, dist)) {
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
