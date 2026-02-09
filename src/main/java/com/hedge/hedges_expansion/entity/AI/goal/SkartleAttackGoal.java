package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.living.SkartleEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SkartleAttackGoal extends GenericMeleeGoal<SkartleEntity> {

    private boolean shouldPathAway;
    @Nullable
    protected Path path;
    private int pathTicks;

    public SkartleAttackGoal(SkartleEntity pMob) {
        super(pMob, 1.6);
    }


    @Override
    public void start() {
        super.start();
        this.shouldPathAway = false;
        this.pathTicks = 0;
    }

    @Override
    protected void tickPath(LivingEntity livingentity) {
        this.attackReach = this.mob.getAttackReachSqr(livingentity);
        this.dist = this.mob.distanceToSqr(livingentity);
        if (this.shouldPathAway || this.mob.canSpit(this.attackReach, this.dist)) {
            this.pathTicks++;
            if (!this.shouldPathAway) {
                Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 12, 6, livingentity.position());
                if (vec3 != null) {
                    this.path = this.mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                    if (this.path != null) {
                        this.shouldPathAway = true;
                        this.mob.getNavigation().moveTo(this.path, 1.6);
                    }

                } else {
                    this.pathTicks = 21;
                }
            }
        }  else if (this.attackReach < this.dist) {
            super.tickPath(livingentity);
        } else {
            this.mob.getLookControl().setLookAt(livingentity);
        }
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        int animState = this.mob.getAnimState();
        if (livingentity != null) {
            this.tickPath(livingentity);
            if (animState == 0 && this.mob.hasLineOfSight(livingentity)) {
                if (this.mob.canUseAttack(livingentity, this.attackReach, this.dist)) {
                    this.mob.setAttacking();
                } else if (this.mob.getNavigation().isDone() || this.pathTicks > 20) {
                    if (this.mob.getSpitCD() == 0) {
                        this.mob.setAnimState(3);
                    }
                    this.pathTicks = 0;
                    this.shouldPathAway = false;
                }
            }

        }
    }
}
