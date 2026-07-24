package com.hedge.hedges_bestiary.entity.AI.goal;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MoveToHomePosGoal extends Goal {

    protected final HBTamableAnimal mob;
    private final double speedModifier;
    private final double startDist;
    private final double stopDist;
    private int closeToHomeTryTicks;


    public MoveToHomePosGoal(HBTamableAnimal mob) {
        this(mob, 1.0, 64D, 4D);
    }

    public MoveToHomePosGoal(HBTamableAnimal mob, double speedModifier, double startDist, double stopDist) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.startDist = startDist;
        this.stopDist = stopDist;
        this.closeToHomeTryTicks = 100 + this.mob.getRandom().nextInt(100);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }


    @Override
    public boolean canUse() {
        if (this.mob.getHomePos() == BlockPos.ZERO) return false;
        this.closeToHomeTryTicks = Math.max(this.closeToHomeTryTicks - 1, 0);
        if (this.closeToHomeTryTicks == 0) {
            this.closeToHomeTryTicks = 100 + this.mob.getRandom().nextInt(100);
            if (!this.mob.getHomePos().closerToCenterThan(this.mob.position(), this.startDist)) {
                return moveToHome();
            }
        }
        return false;

    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getHomePos().closerToCenterThan(this.mob.position(), this.stopDist); //&& !this.mob.getNavigation().isDone();
    }

    @Override
    public void tick() {
        if (this.mob.getNavigation().isDone()) {
            this.moveToHome();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.closeToHomeTryTicks = 100 + this.mob.getRandom().nextInt(100);
    }

    protected boolean moveToHome() {

        BlockPos pos = this.mob.getHomePos();
        this.mob.getNavigation().setMaxVisitedNodesMultiplier(10.0F);
        this.mob.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), this.speedModifier);
        return this.mob.getNavigation().getPath() != null && this.mob.getNavigation().getPath().canReach();

    }

}
