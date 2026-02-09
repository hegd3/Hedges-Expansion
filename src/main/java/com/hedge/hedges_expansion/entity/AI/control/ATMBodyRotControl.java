package com.hedge.hedges_expansion.entity.AI.control;

import com.hedge.hedges_expansion.entity.util.AdvancedTurningMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class ATMBodyRotControl<T extends Mob & AdvancedTurningMob> extends BodyRotationControl {

    protected final T entity;

    public ATMBodyRotControl(T pMob) {
        super(pMob);
        this.entity = pMob;
    }

    @Override
    public void clientTick() {
        if (this.entity.shouldInstantTurn() || this.entity.shouldTurnWholeBody()) {
            this.entity.yHeadRot = this.entity.getYRot();
            this.entity.yBodyRot = this.entity.yHeadRot;
        } else {
            super.clientTick();
        }
    }
}
