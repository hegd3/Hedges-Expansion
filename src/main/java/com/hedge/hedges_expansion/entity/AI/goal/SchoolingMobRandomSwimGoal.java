package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HESchoolingMob;

public class SchoolingMobRandomSwimGoal extends HECustomSwimGoal {
    private final HESchoolingMob entity;
    public SchoolingMobRandomSwimGoal(HESchoolingMob mob, double speedModifier, int interval, int radius, int height, boolean preferSurface) {
        super(mob, speedModifier, interval, radius, height, preferSurface);
        this.entity = mob;
    }

    @Override
    public boolean canUse() {
        return this.entity.canRandomSwim() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.canRandomSwim() && super.canContinueToUse();
    }
}
