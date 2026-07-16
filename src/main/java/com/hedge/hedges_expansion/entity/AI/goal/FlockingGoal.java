package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HEGroupMob;
import com.hedge.hedges_expansion.entity.types.TamableFlyer;

public class FlockingGoal<E extends TamableFlyer & HEGroupMob<E>> extends GroupFollowLeaderGoal<E> {
    public FlockingGoal(E mob) {
        super(mob);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mob.tickCount % 10 == 0) {
            if (this.mob.getLeader().isFlying() && !this.mob.isFlying()) {
                this.mob.setFlying(true);
                this.mob.setLanding(this.mob.getLeader().isLanding());
            }
        }
    }
}
