package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.types.HESemiFlyer;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import com.hedge.hedges_expansion.entity.util.WorldHelpers;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
    private Vec3 pos;

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
        if (!this.mob.getNavigation().isDone() || this.mob.getRandom().nextInt(interval) != 0) {
            return false;
        }

        this.pos = this.findPos();
        if (this.pos != null) {
            this.mob.setFlying(true);
            this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(BlockPos.containing(this.pos), 1), 1.0D);
            this.ticksFlying = 0;
            return true;
        }
        return false;
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
        return true;
    }


    @Override
    public void stop() {
        super.stop();
        this.mob.setFlying(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksFlying++;
        this.mob.lookAt(EntityAnchorArgument.Anchor.EYES, pos);

        if (this.ticksFlying > this.maxTicksFlying || this.mob.getNavigation().isDone()) {

            System.out.println("attempting to find pos");

            Vec3 vec3 = this.findGroundPos();
            if (vec3 != null) {
                System.out.println("found ground pos");

                this.pos = vec3;
                this.mob.getNavigation().moveTo(this.pos.x, this.pos.y, this.pos.z, 1.0f);

            } else {
                vec3 = this.findPos();
                if (vec3 != null) {
                    System.out.println("found new pos");
                    this.pos = vec3;
                    this.ticksFlying = 0;
                    this.mob.getNavigation().moveTo(this.pos.x, this.pos.y, this.pos.z, 1.0f);
                }
            }
        }
        if (this.ticksFlying % 10 == 0) {
            if (this.ticksFlying < this.maxTicksFlying) {
                BlockPos blockPos = WorldHelpers.fromVec3(this.pos);
                int blocksFromBoundary = EntityHelpers.blocksFromGround(this.mob.level(), blockPos, this.targetY * 2);
                if (blocksFromBoundary < this.targetY) {
                    this.pos = this.pos.add(0, 4, 0);
                    this.mob.getNavigation().moveTo(this.pos.x, this.pos.y, this.pos.z, 1.0f);
                }

            } else {
                this.pos = this.pos.add(0, -1, 0);
                this.mob.getNavigation().moveTo(this.pos.x, this.pos.y, this.pos.z, 1.0f);
            }
        }
    }

    @Nullable
    private Vec3 findPos() {
        return EntityHelpers.getSmartFlyingTarget(this.mob, this.radius, yRange, targetY, targetY * 2);
    }

    @Nullable
    private Vec3 findGroundPos() {
        return EntityHelpers.getSmartFlyingTarget(this.mob, this.radius, yRange, 0, targetY * 2);
    }
}
