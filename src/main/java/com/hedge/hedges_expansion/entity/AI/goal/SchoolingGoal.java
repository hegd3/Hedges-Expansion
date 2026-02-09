package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HESchoolingMob;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;

import java.util.List;
import java.util.function.Predicate;

public class SchoolingGoal extends Goal {
    private static final int INTERVAL_TICKS = 200;
    private final HESchoolingMob mob;
    private int timeToRecalcPath;
    private int nextStartTick;

    public SchoolingGoal(HESchoolingMob pFish) {
        this.mob = pFish;
        this.nextStartTick = this.nextStartTick(pFish);
    }

    protected int nextStartTick(HESchoolingMob pTaskOwner) {
        return reducedTickDelay(200 + pTaskOwner.getRandom().nextInt(200) % 20);
    }

    public boolean canUse() {
        if (this.mob.hasFollowers()) {
            return false;
        } else if (this.mob.isFollower()) {
            return true;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            Predicate<HESchoolingMob> predicate = (p_25258_) -> {
                return p_25258_.canBeFollowed() || !p_25258_.isFollower();
            };
            List<? extends HESchoolingMob> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
            HESchoolingMob schoolingMob = DataFixUtils.orElse(list.stream().filter(HESchoolingMob::canBeFollowed).findAny(), this.mob);
            schoolingMob.addFollowers(list.stream().filter((p_25255_) -> {
                return !p_25255_.isFollower();
            }));
            return this.mob.isFollower();
        }
    }

    public boolean canContinueToUse() {
        return this.mob.isFollower() && this.mob.inRangeOfLeader();
    }


    public void start() {
        this.timeToRecalcPath = 0;
    }


    public void stop() {
        this.mob.stopFollowing();
    }


    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.mob.pathToLeader();
        }
    }
}
