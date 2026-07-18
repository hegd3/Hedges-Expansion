package com.hedge.hedges_bestiary.entity.types;

public interface AdvancedTurningMob {

    public boolean shouldTurnWholeBody();

    public boolean shouldLockAngle();

    public boolean shouldInstantTurn();

    public float getTurnSpeed();

}
