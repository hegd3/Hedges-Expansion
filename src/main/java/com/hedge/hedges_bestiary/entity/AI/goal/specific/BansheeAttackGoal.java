package com.hedge.hedges_bestiary.entity.AI.goal.specific;

import com.hedge.hedges_bestiary.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_bestiary.entity.living.BansheeEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class BansheeAttackGoal extends GenericMeleeGoal<BansheeEntity> {

    private int ticksOrbiting;

    public BansheeAttackGoal(BansheeEntity pMob) {
        super(pMob, 1.4);
    }

    @Override
    protected void tickPath(LivingEntity livingentity) {
        if (this.attackReach < this.dist) {
            super.tickPath(livingentity);
        } else {
            this.ticksOrbiting++;
            float zoomIn = 1F - ticksOrbiting / 200f;
            Vec3 orbitPos = orbitAroundPos(livingentity, 100 + zoomIn * 6.0F).add(0, 20 + zoomIn * 6, 0);
            mob.getNavigation().moveTo(orbitPos.x, orbitPos.y, orbitPos.z, 1.4);
            mob.lookAt(EntityAnchorArgument.Anchor.EYES, orbitPos);
            if (this.mob.getAnimState() == 0 && this.mob.canSpin()) {
                this.mob.setAnimState(1);
            }
        }
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        int animState = this.mob.getAnimState();
        if (livingentity != null && animState < 2) {
            this.attackReach = this.mob.getAttackReachSqr(livingentity);
            this.dist = this.mob.distanceToSqr(livingentity);
            if (this.ticksOrbiting > 80 && animState == 0) {
                if (this.mob.canUseAttack(livingentity, attackReach, dist)) {
                    this.mob.setAttacking();
                    this.ticksOrbiting = 0;
                } else if (this.mob.canDive(attackReach, dist, this.mob.getY() - livingentity.getY())) {
                    this.mob.getNavigation().stop();
                    this.mob.setAnimState(4);
                    this.ticksOrbiting = 0;
                }
            } else {
                this.tickPath(livingentity);
            }
        }
    }

    private Vec3 orbitAroundPos(LivingEntity target, float circleDistance) {
        final float angle = 3 * (float) (Math.toRadians(ticksOrbiting * 3F));
        final double extraX = circleDistance * Mth.sin((angle));
        final double extraZ = circleDistance * Mth.cos(angle);
        return target.getEyePosition().add(extraX, 0, extraZ);
    }
}
