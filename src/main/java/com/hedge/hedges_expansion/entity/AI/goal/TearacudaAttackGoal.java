package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.living.TearacudaEntity;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class TearacudaAttackGoal extends GenericMeleeGoal<TearacudaEntity> {


    public TearacudaAttackGoal(TearacudaEntity entity) {
        super(entity, 1.3);
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            this.attackReach = this.mob.getAttackReachSqr(livingentity);
            this.dist = this.mob.distanceToSqr(livingentity);
            if (this.mob.isInFluidType()) {
                if (this.attackReach < this.dist) {
                    this.tickPath(livingentity);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity);
                }
            }
        }

        if (this.mob.getAnimState() == 0) {
            if (this.mob.canFrenzy(this.attackReach, this.dist)) {
                this.mob.setAnimState(2);
            }
            else if (this.mob.canUseAttack(livingentity, this.attackReach, this.dist)) {
                this.mob.setAttacking();
            } else if (this.mob.canJump(this.attackReach, this.dist)) {
                Vec3 towardTarget = livingentity.position().subtract(this.mob.position()).normalize();
                Vec3 dashVector = new Vec3(towardTarget.x, 0.67, towardTarget.z).normalize().scale(1.2);
                this.mob.setDeltaMovement(dashVector);
                float yaw = (float)(Mth.atan2(dashVector.z, dashVector.x) * (180F / Math.PI)) - 90.0F;
                float pitch = (float)(-(Mth.atan2(dashVector.y, Mth.sqrt((float)(dashVector.x * dashVector.x + dashVector.z * dashVector.z))) * (180F / Math.PI))) * 0.5f;

                this.mob.setYHeadRot(yaw);
                this.mob.setYRot(yaw);

                this.mob.setXRot(pitch);
            }
        }
    }
}
