package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HESemiFlyer;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SemiFlyerFlyingGoal<E extends PathfinderMob & HESemiFlyer> extends Goal {

    private final E mob;
    private final int maxTicksFlying;
    private final int interval;
    private final int yRange;
    private final int radius;
    private final int targetY;
    private int ticksFlying ;

    public SemiFlyerFlyingGoal(E mob, int maxTicksFlying, int radius, int yRange, int targetY, int interval) {
        this.mob = mob;
        this.maxTicksFlying = maxTicksFlying;
        this.yRange = yRange;
        this.radius = radius;
        this.targetY = targetY;
        this.interval = interval;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isInFluidType()) {
            if (!EntityHelpers.closeToSurface(this.mob, 2)) {
                return false;
            }
        }
        return this.mob.getNavigation().isDone() && this.mob.getRandom().nextInt(interval) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isInFluidType()) {
            if (!EntityHelpers.closeToSurface(this.mob, 2)) {
                return false;
            }
        }
        else if (this.ticksFlying > 10 && this.mob.onGround()) {
            return false;
        }
        else if (this.ticksFlying > this.maxTicksFlying || this.mob.getNavigation().isDone()) {
            Vec3 vec3 = this.findGroundPos();
            if (vec3 != null) {
                this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(BlockPos.containing(vec3), 1), 1.0D);
                this.ticksFlying = 0;
                return true;
            } else {
                vec3 = this.findPos();
                if (vec3 != null) {
                    this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(BlockPos.containing(vec3), 1), 1.0D);
                    this.ticksFlying = 0;
                    return true;
                }
            }
        }
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        Vec3 vec3 = this.findPos();
        if (vec3 != null) {
            this.mob.setFlying(true);
            this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(BlockPos.containing(vec3), 1), 1.0D);
            this.ticksFlying = 0;
        }

    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setFlying(false);
    }

    @Override
    public void tick() {
        super.tick();
        ticksFlying++;
    }

    @Nullable
    private Vec3 findPos() {
        return EntityHelpers.getSmartFlyingTarget(this.mob, this.radius, yRange, targetY, 1);
    }

    @Nullable
    private Vec3 findGroundPos() {
        return EntityHelpers.getSmartFlyingTarget(this.mob, this.radius, yRange, 1, 1);
    }
}
