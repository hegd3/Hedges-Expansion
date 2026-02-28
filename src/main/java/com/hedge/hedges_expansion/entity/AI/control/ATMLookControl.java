package com.hedge.hedges_expansion.entity.AI.control;

import com.hedge.hedges_expansion.entity.types.AdvancedTurningMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class ATMLookControl<E extends Mob & AdvancedTurningMob> extends LookControl {
    private final E entity;


    public ATMLookControl(E pMob) {
        super(pMob);
        this.entity = pMob;
    }

    public void tick() {
        if (!entity.shouldLockAngle()) {
            this.yMaxRotSpeed = entity.getTurnSpeed();
            if (entity.shouldInstantTurn()) {
                float targetYRot = this.getYRotD().orElse(this.mob.getYRot());
                float targetXRot = this.getXRotD().orElse(this.mob.getXRot());

                this.mob.setYRot(targetYRot);
                this.mob.yBodyRot = targetYRot;
                this.mob.yHeadRot = targetYRot;
                this.mob.setXRot(targetXRot);

            /*
            this.mob.yBodyRotO = targetYRot;
            this.mob.yHeadRotO = targetYRot;
            this.mob.xRotO = targetXRot;

             */
            } else if (entity.shouldTurnWholeBody()) {
                float targetYRot = this.getYRotD().orElse(this.mob.getYRot());
                float targetXRot = this.getXRotD().orElse(this.mob.getXRot());

                float yRot = this.rotateTowards(this.mob.getYRot(), targetYRot, this.yMaxRotSpeed);
                float bodyYRot = this.rotateTowards(this.mob.yBodyRot, targetYRot, this.yMaxRotSpeed);
                float headYRot = this.rotateTowards(this.mob.yHeadRot, targetYRot, this.yMaxRotSpeed);
                float xRot = this.rotateTowards(this.mob.getXRot(), targetXRot, this.xMaxRotAngle);

                this.mob.setYRot(yRot);
                this.mob.yBodyRot = bodyYRot;
                this.mob.yHeadRot = headYRot;
                this.mob.setXRot(xRot);

            /*
            this.mob.yRotO = yRot;
            this.mob.yBodyRotO = yRot;
            this.mob.yHeadRotO = headYRot;
            this.mob.xRotO = xRot;

             */

            } else {
                super.tick();
            }
        }
    }
}
