package com.hedge.hedges_bestiary.entity.AI.goal;

import com.hedge.hedges_bestiary.entity.types.TamableFlyer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.phys.Vec3;

public class FlyerMoveToHomePosGoal extends MoveToHomePosGoal {
    private final TamableFlyer flyer;

    public FlyerMoveToHomePosGoal(TamableFlyer mob, double speedModifier, double startDist, double stopDist) {
        super(mob, speedModifier, startDist, stopDist);
        this.flyer = mob;
    }

    public FlyerMoveToHomePosGoal(TamableFlyer mob) {
        super(mob);
        this.flyer = mob;
    }

    @Override
    public void start() {
        super.start();
        if (!this.flyer.isFlying()) {
            this.flyer.setFlying(true);
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (this.flyer.isFlying()) {
            this.flyer.setFlying(false);
        }
    }




}
