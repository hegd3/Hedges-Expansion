package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.living.BehemothEntity;
import net.minecraft.world.entity.LivingEntity;

public class BehemothAttackGoal extends GenericMeleeGoal<BehemothEntity> {


    public BehemothAttackGoal(BehemothEntity entity) {
        super(entity, 1.5f);
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        int animState = this.mob.getAnimState();
        if (this.canPath() && livingentity != null) {
            this.attackReach = this.mob.getAttackReachSqr(livingentity);
            this.dist = this.mob.distanceToSqr(livingentity);
            if (this.attackReach < this.dist) {
                this.tickPath(livingentity);
            } else {
                this.mob.getLookControl().setLookAt(livingentity);
            }
            if (animState == 0) {
                if (this.mob.canCharge(attackReach, dist)) {
                    this.mob.setAnimState(BehemothEntity.CHARGE_STARTUP_ANIM);
                } else if (this.mob.canJump(attackReach, dist)) {
                    this.mob.setAnimState(BehemothEntity.JUMP_ANIM);
                }
                else if (this.mob.canUseAttack(livingentity, attackReach, dist)) {
                    this.mob.setAttacking();
                } else if (this.mob.canRoar()) {
                    this.mob.setAnimState(BehemothEntity.ROAR_ANIM);
                }
            }
        }
    }

    @Override
    protected double getSpeedModifier() {
        return switch (this.mob.getAnimState()) {
            case 1, 2 -> 1;
            default -> super.getSpeedModifier();
        };
    }

    public boolean canPath() {
        return switch(this.mob.getAnimState()) {
            case BehemothEntity.ARM_SLAM_ANIM, BehemothEntity.BODY_SLAM_ANIM, BehemothEntity.CHARGE_STARTUP_ANIM, BehemothEntity.CHARGE_ANIM,
                 BehemothEntity.JUMP_ANIM, BehemothEntity.LAND_ANIM -> false;
            default -> true;
        };
    }
}
