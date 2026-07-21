package com.hedge.hedges_bestiary.entity.AI.goal;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class NapGoal extends Goal {

    private final HBTamableAnimal mob;
    private final SleepType sleepType;
    private final boolean sleepsInWater;
    private int napCD;

    public NapGoal(HBTamableAnimal mob) {
        this(mob, SleepType.DIURNAL, false);
    }

    public NapGoal(HBTamableAnimal mob, SleepType sleepType, boolean sleepsInWater) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        this.mob = mob;
        this.sleepType = sleepType;
        this.sleepsInWater = sleepsInWater;
        this.resetNapCD();
    }

    @Override
    public boolean canUse() {
        if (this.mob.isNapping()) {
            return true;
        }
        if (!this.sleepsInWater && (this.mob.isInFluidType() || !this.mob.onGround())) {
            return false;
        }
        this.napCD = Math.max(this.napCD - 1, 0);
        return this.napCD == 0 && this.canSleep();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setNapping(true);
        this.resetNapCD();
    }

    @Override
    public void stop() {
        this.mob.setNapping(false);
        if (this.mob.isOrderedToSit() && !this.mob.isSitting()) {
            this.mob.setSitting(true);
        }
    }

    @Override
    public boolean canContinueToUse() {
        this.napCD = Math.max(this.napCD - 1, 0);
        if (!this.mob.isNapping()) {
            return false;
        }
        if (!this.sleepsInWater && this.mob.isInFluidType()) {
            return false;
        }
        this.napCD = Math.max(this.napCD - 1, 0);
        if (this.napCD == 0) {
            return this.canSleep();
        }
        return true;
    }

    private boolean canSleep() {
        this.resetNapCD();
        long dayTime = this.mob.level().getDayTime();
        return switch (this.sleepType) {
            case DIURNAL -> dayTime > 13000 || dayTime < 6000;
            case NOCTURNAL -> dayTime < 23000 && dayTime > 6000;
            case CATHERMAL -> (dayTime < 12000 || dayTime > 18000) && dayTime < 23000 && dayTime > 8000; // active "randomly"
            case MATUTINAL -> dayTime > 23000 || dayTime < 1000; // active at sunrise
            case VESPERTINE -> dayTime < 12000 || dayTime > 18000;
        };
    }

    private void resetNapCD() {
        this.napCD = 60 + this.mob.getRandom().nextInt(120);
    }

    public enum SleepType {
        DIURNAL,
        NOCTURNAL,
        CATHERMAL,
        MATUTINAL,
        VESPERTINE,

    }
}
