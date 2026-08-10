package com.hedge.hedges_bestiary.entity.types;

public interface IdleAnimMob {

    public void playIdle();

    public boolean canPlayIdle();

    public void playStaticIdle();

    public boolean canPlayStaticIdle();

    public boolean isStaticIdling();
}
