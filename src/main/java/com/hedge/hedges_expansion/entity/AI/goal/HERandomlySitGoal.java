package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HETamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HERandomlySitGoal extends Goal {

    private final HETamableAnimal mob;
    private final int cooldown;
    private final int maxTicksSitting;


    private int ticksSitting;
    private int ticksTillSit;

    public HERandomlySitGoal(HETamableAnimal mob) {
        this(mob, 500, 500);
    }

    public HERandomlySitGoal(HETamableAnimal mob, int cooldown, int maxTicksSitting) {
        this.mob = mob;
        this.cooldown = cooldown;
        this.maxTicksSitting = maxTicksSitting;
        this.ticksTillSit = this.cooldown * (int)(this.mob.getRandom().nextDouble() + 1);
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setInSittingPose(true);
        this.mob.setSitting(true);
        this.ticksSitting = 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isTame() || this.mob.getTarget() != null)
            return false;
        else if (this.mob.isInFluidType() || !this.mob.onGround())
            return false;
        return this.ticksSitting <  this.maxTicksSitting;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isTame() || this.mob.getTarget() != null)
            return false;
        else if (this.mob.isInFluidType() || !this.mob.onGround())
            return false;

        if (this.mob.isSitting()) {
            return true;
        }
        this.ticksTillSit = Math.max(this.ticksTillSit - 1, 0);
        return this.ticksTillSit == 0;
    }

    @Override
    public void tick() {
        this.ticksSitting++;
    }

    @Override
    public void stop() {
        this.mob.setInSittingPose(false);
        if (this.mob.isSitting() && this.mob.getCommand() != 1) {
            this.mob.setSitting(false);
        }
        this.ticksTillSit = this.cooldown * (int)(this.mob.getRandom().nextDouble() + 1);

    }
}
