package com.hedge.hedges_bestiary.entity.AI.goal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;

import java.util.EnumSet;

public class HBHurtByTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private int timestamp;
    private int changeTargetCD;
    public HBHurtByTargetGoal(Mob pMob) {
        super(pMob, true);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        int i = this.mob.getLastHurtByMobTimestamp();
        LivingEntity livingentity = this.mob.getLastHurtByMob();
        if (i != this.timestamp && livingentity != null) {
            if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                return this.canAttack(livingentity, HURT_BY_TARGETING);
            }
        } else {
            return false;
        }
    }
    @Override
    public void start() {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        this.changeTargetCD = 60;

        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.changeTargetCD-- == 0) {
            LivingEntity lastHurt = this.mob.getLastHurtByMob();
            if (lastHurt != null && lastHurt != this.targetMob) {
                this.mob.setTarget(lastHurt);
                this.targetMob = this.mob.getTarget();
                this.timestamp = this.mob.getLastHurtByMobTimestamp();
            }
            this.changeTargetCD = 60;
        }
        return super.canContinueToUse();
    }
}
