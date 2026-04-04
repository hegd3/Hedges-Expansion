package com.hedge.hedges_expansion.entity.types;

public interface HESemiFlyer {

    boolean isFlying();

    void setFlying(boolean b);

    boolean isLandNav();

    boolean isLanding();

    void setLanding(boolean b);

    int getGroundTicks();

}
