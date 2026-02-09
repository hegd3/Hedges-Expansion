package com.hedge.hedges_expansion.entity.AI.goal;

import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HECustomSwimGoal extends RandomStrollGoal {

    private final int radius;
    private final int height;
    private final boolean preferSurface;

    public HECustomSwimGoal(PathfinderMob mob, double speedModifier, int interval, int radius, int height, boolean preferSurface) {
        super(mob, speedModifier, interval);
        this.radius = radius;
        this.height = height;
        this.preferSurface = preferSurface;
    }

    @Override
    public boolean canUse() {
        return this.mob.isInFluidType() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isInFluidType() && super.canContinueToUse();
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        return EntityHelpers.getSmartSwimTarget(this.mob, this.radius, this.height, this.preferSurface);
    }
}
