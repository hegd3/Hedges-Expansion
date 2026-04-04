package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HETamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HESitWhenOrderedGoal extends Goal {
    private final boolean standsInWater;
    private final HETamableAnimal mob;

    public HESitWhenOrderedGoal(HETamableAnimal mob) {
        this(mob, true);
    }

    public HESitWhenOrderedGoal(HETamableAnimal mob, boolean standsInWater) {
       this.mob = mob;
       this.standsInWater = standsInWater;
       this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canContinueToUse() {
        return this.mob.isOrderedToSit();
    }

    public boolean canUse() {
        if (!this.mob.isTame()) {
            return false;
        } else if (this.standsInWater) {
            if (this.mob.isInFluidType() || !this.mob.onGround()) {
                return false;
            }
        }
        LivingEntity livingentity = this.mob.getOwner();
        if (livingentity == null) {
            return true;
        } else {
            return this.mob.isOrderedToSit() && (!(this.mob.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null);
        }
    }

    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setInSittingPose(true);
        this.mob.setSitting(true);
    }

    public void stop() {
        this.mob.setInSittingPose(false);
        if (this.mob.isSitting() && this.mob.getCommand() != 1) {
            this.mob.setSitting(false);
        }
    }

}
