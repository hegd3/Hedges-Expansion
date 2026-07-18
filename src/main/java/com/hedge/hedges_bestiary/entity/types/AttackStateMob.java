package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.world.entity.LivingEntity;

public interface AttackStateMob {

    public void setAttacking();

    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist);

    public double getAttackReachSqr(LivingEntity entity);

}
