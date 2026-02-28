package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HESemiFlyer;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import com.ibm.icu.text.PluralRules;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SemiFlyerCircleWanderGoal<E extends PathfinderMob & HESemiFlyer> extends Goal {

    private final E mob;
    private Vec3 startOrbitFrom;
    private int ticksFlying;

    private final int maxOrbitTime;
    private final int interval;
    private final int yRange;
    private final int radius;
    private final int targetY;
    private final float circleDistance;
    private int increaseAttempts;

    public SemiFlyerCircleWanderGoal(E mob, int interval, int maxOrbitTime, int radius, int yRange, int targetY, float circleDistance) {
        this.mob = mob;
        this.interval = interval;
        this.maxOrbitTime = maxOrbitTime;
        this.radius = radius;
        this.yRange = yRange;
        this.targetY = targetY;
        this.circleDistance = circleDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public void start() {
        this.ticksFlying = 0;
        this.increaseAttempts = yRange;
        this.mob.setFlying(true);

    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setFlying(false);
    }

    @Override
    public boolean canUse() {
        if (this.mob.isInFluidType()) {
            if (!EntityHelpers.closeToSurface(this.mob, 2)) {
                return false;
            }
        }
        this.startOrbitFrom = this.getPos(EntityHelpers.blocksFromGround(this.mob, targetY));
        return this.startOrbitFrom != null && this.mob.getNavigation().isDone() && this.mob.getRandom().nextInt(interval) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isInFluidType()) {
            if (!EntityHelpers.closeToSurface(this.mob, 2)) {
                return false;
            }
        } else if (this.ticksFlying > 10 && this.mob.onGround()) {
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        int blocksFromGround = EntityHelpers.blocksFromGround(this.mob, targetY);

        if (this.ticksFlying > this.maxOrbitTime) {
            Vec3 v = this.getPos(blocksFromGround);
            if (v != null) {
                this.startOrbitFrom = v;
                this.ticksFlying = 0;
                this.increaseAttempts = targetY;
            }
        }
        else if (this.increaseAttempts > 0 && blocksFromGround != targetY) {
            this.startOrbitFrom = this.startOrbitFrom.add(0, targetY > blocksFromGround ? 1 : -1, 0);
            this.increaseAttempts--;
        }
        ticksFlying++;
        float zoomIn = 1F - ticksFlying / (float) maxOrbitTime;
        Vec3 orbitPos = orbitAroundPos(circleDistance + zoomIn * 5.0F).add(0, targetY + zoomIn * 3, 0);
        mob.getNavigation().moveTo(orbitPos.x, orbitPos.y, orbitPos.z, 1);
        mob.lookAt(EntityAnchorArgument.Anchor.EYES, orbitPos);

    }


    public Vec3 orbitAroundPos(float circleDistance) {
        final float angle = 3 * (float) (Math.toRadians(ticksFlying * 3F));
        final double extraX = circleDistance * Mth.sin((angle));
        final double extraZ = circleDistance * Mth.cos(angle);
        return startOrbitFrom.add(extraX, 0, extraZ);
    }

    @Nullable
    private Vec3 getPos(int blocksFromGround) {
        return EntityHelpers.getSmartFlyingTarget(this.mob, this.radius, yRange, targetY, blocksFromGround);
    }

}
