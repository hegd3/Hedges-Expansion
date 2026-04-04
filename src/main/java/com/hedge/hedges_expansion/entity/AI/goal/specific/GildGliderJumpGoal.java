package com.hedge.hedges_expansion.entity.AI.goal.specific;

import com.hedge.hedges_expansion.entity.AI.goal.JumpFromWaterGoal;
import com.hedge.hedges_expansion.entity.living.ambientfish.GildGliderEntity;

public class GildGliderJumpGoal extends JumpFromWaterGoal {

    private final GildGliderEntity mob;

    public GildGliderJumpGoal(GildGliderEntity mob) {
        super(mob, 10, 0.8);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {

        if (!this.mob.isFollower()) {
            if (this.mob.getRandom().nextInt(this.interval) != 0) {
                return false;
            }
            return this.canJump();
        } else if (((GildGliderEntity)this.mob.getLeader()).canJump()) {
            return this.canJump();
        }
        return false;

    }

    @Override
    public void start() {
        super.start();
        this.mob.setCanJump(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setCanJump(false);
    }
}
