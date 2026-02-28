package com.hedge.hedges_expansion.entity.AI.control;

import com.hedge.hedges_expansion.entity.types.AdvancedTurningMob;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ATMSemiaquaticMoveControl<E extends Mob & AdvancedTurningMob> extends ATMMoveControl<E> {

    private static final float FULL_SPEED_TURN_THRESHOLD = 10.0F;
    private static final float STOP_TURN_THRESHOLD = 60.0F;
    private final int maxTurnX;
    private final float inWaterSpeedModifier;


    public ATMSemiaquaticMoveControl(E pMob, int pMaxTurnX, float pInWaterSpeedModifier) {
        super(pMob);
        this.maxTurnX = pMaxTurnX;
        this.inWaterSpeedModifier = pInWaterSpeedModifier;
    }

    @Override
    public void tick() {
        if (this.mob.isInFluidType()) {
            if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedY - this.mob.getY();
                double d2 = this.wantedZ - this.mob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                } else {
                    float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    if (!this.entity.shouldLockAngle()) {
                        this.mob.setYRot(this.entity.shouldInstantTurn() ? f : this.rotlerp(this.mob.getYRot(), f, this.entity.getTurnSpeed()));
                        this.mob.yBodyRot = this.mob.getYRot();
                        this.mob.yHeadRot = this.mob.getYRot();
                    }
                    float f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    if (this.mob.horizontalCollision) {
                        final float outWater = this.mob.getYRot();
                        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-Mth.sin(outWater) * 0.2f, 0.1, Mth.cos(outWater) * 0.2f));
                    }
                    this.mob.setSpeed(f1 * this.inWaterSpeedModifier);
                    double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                    if ((!this.entity.shouldLockAngle()) && (Math.abs(d1) > (double)1.0E-5F || Math.abs(d4) > (double)1.0E-5F)) {
                        float f3 = -((float)(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                        f3 = Mth.clamp(Mth.wrapDegrees(f3), (float)(-this.maxTurnX), (float)this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f3, 5.0F));
                    }

                    float f6 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                    float f4 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                    this.mob.zza = f6 * f1;
                    this.mob.yya = -f4 * f1;
                }
            }
            else {
                this.mob.setSpeed(0.0F);
                this.mob.setXxa(0.0F);
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }

        }
        else {
            super.tick();
        }
    }


    private static float getTurningSpeedFactor(float p_249853_) {
        return 1.0F - Mth.clamp((p_249853_ - 10.0F) / 50.0F, 0.0F, 1.0F);
    }
}
