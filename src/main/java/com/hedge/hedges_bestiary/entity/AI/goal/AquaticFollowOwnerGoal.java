package com.hedge.hedges_bestiary.entity.AI.goal;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;

public class AquaticFollowOwnerGoal extends HBFollowOwnerGoal {

    public AquaticFollowOwnerGoal(HBTamableAnimal mob, double speedModifier, double sprintSpeedModifier, float startDistance, float stopDistance) {
        super(mob, speedModifier, sprintSpeedModifier, startDistance, stopDistance);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(owner, 10.0F, (float) mob.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (this.mob.distanceToSqr(owner) >= 256.0D) {
                this.teleportToOwner();
            } else if (this.mob.isInWater()) {
                if (this.mob.distanceToSqr(owner) >= 64.0D) {
                    this.mob.getNavigation().moveTo(owner, sprintSpeedModifier);
                } else{
                    this.mob.getNavigation().moveTo(owner, speedModifier);
                }
            }
        }
    }

    @Override
    protected boolean canTeleportTo(BlockPos blockPos) {
        Level level = this.mob.level();
        if (level.getFluidState(blockPos).is(FluidTags.WATER) || !level.getFluidState(blockPos).is(FluidTags.WATER) && level.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
            BlockPos pos = blockPos.subtract(this.mob.blockPosition());
            return level.noCollision(this.mob, this.mob.getBoundingBox().move(pos));
        }
        return false;
    }


}
