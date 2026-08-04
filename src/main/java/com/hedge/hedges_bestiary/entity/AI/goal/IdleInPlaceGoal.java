package com.hedge.hedges_bestiary.entity.AI.goal;

import com.hedge.hedges_bestiary.entity.types.IdleAnimMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class IdleInPlaceGoal<E extends PathfinderMob & IdleAnimMob> extends IdleAnimationGoal<E> {

    public IdleInPlaceGoal(E pMob) {
        this(pMob, 100);
    }

    public IdleInPlaceGoal(E pMob, int cooldown) {
        super(pMob, cooldown);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public void start() {
        this.resetCD();
        this.mob.getNavigation().stop();
    }
}
