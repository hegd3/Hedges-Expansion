package com.hedge.hedges_expansion.entity.AI.goal.specific;

import com.hedge.hedges_expansion.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_expansion.entity.living.GruinEntity;
import net.minecraft.world.entity.LivingEntity;

public class GruinAttackGoal extends GenericMeleeGoal<GruinEntity> {
    public GruinAttackGoal(GruinEntity pMob) {
        super(pMob, 1.3);
    }

    @Override
    public boolean canUse() {
        return !this.mob.isBaby() && super.canUse();
    }

    @Override
    protected double getSpeedModifier() {
        return switch (this.mob.getAnimState()) {
            case 1, 2 -> 1;
            default -> super.getSpeedModifier();
        };
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            this.attackReach = this.mob.getAttackReachSqr(livingentity);
            this.dist = this.mob.distanceToSqr(livingentity);
            if (this.mob.getAnimState() != 3) {
                if (this.attackReach < this.dist) {
                    this.tickPath(livingentity);
                } else {
                    this.mob.lookAt(livingentity, 30f, 30f);
                    this.mob.getLookControl().setLookAt(livingentity, 30f, 30f);
                }

            }
            if (this.mob.getAnimState() == 0) {
                if (this.mob.canUseMultiAttack(this.attackReach, this.dist)) {
                    this.mob.setAnimState(3);
                } else if (this.mob.canUseAttack(livingentity, this.attackReach, this.dist)) {
                    this.mob.setAttacking();
                }
            }
        }
    }
}
