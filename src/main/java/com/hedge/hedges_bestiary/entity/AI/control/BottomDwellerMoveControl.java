package com.hedge.hedges_bestiary.entity.AI.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class BottomDwellerMoveControl extends MoveControl {
    private final float maxTurnY;
    public BottomDwellerMoveControl(Mob pMob, float maxTurnY) {
        super(pMob);
        this.maxTurnY = maxTurnY;
    }

    @Override
    public void tick() {

        if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 /= d3;
            float f = (float) (Mth.atan2(d2, d0) * (double) (Mth.DEG_TO_RAD)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, maxTurnY));
            this.mob.yBodyRot = this.mob.getYRot();
            float f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float f2 = Mth.lerp(0.125F, this.mob.getSpeed(), f1);
            this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), f1));
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add((double) f2 * d0 * 0.005D, (double) f2 * d1 * 0.1D, (double) f2 * d2 * 0.005D));
        } else {
            this.mob.setSpeed(0.0F);
        }
    }
}
