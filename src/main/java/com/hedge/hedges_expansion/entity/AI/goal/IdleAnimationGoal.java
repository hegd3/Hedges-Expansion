package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.IdleAnimMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class IdleAnimationGoal<E extends PathfinderMob & IdleAnimMob> extends Goal {
    protected final E mob;
    private final int cooldown;
    private int animCD;

    public IdleAnimationGoal(E pMob) {
        this(pMob, 100);
    }

    public IdleAnimationGoal(E pMob, int cooldown) {
        this.mob = pMob;
        this.cooldown = cooldown;
        this.animCD = this.cooldown;
    }

    @Override
    public void start() {
        this.mob.playIdle();
        this.animCD = this.cooldown * (int)(this.mob.getRandom().nextDouble() + 1);
    }

    @Override
    public boolean canUse() {
        this.animCD = Math.max(this.animCD - 1, 0);
        if (this.animCD == 0) {
            return this.mob.canPlayIdle();
        }
        return false;
    }
}
