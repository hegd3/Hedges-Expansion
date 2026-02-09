package com.hedge.hedges_expansion.entity.util;

import net.minecraft.world.entity.LivingEntity;

public interface AttackStateMob {

    public void setAttacking();

    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist);

    public double getAttackReachSqr(LivingEntity entity);

}
