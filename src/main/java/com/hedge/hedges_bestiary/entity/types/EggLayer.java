package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.world.level.block.state.BlockState;

public interface EggLayer {

    public BlockState getEgg();

    public boolean hasEgg();

    default boolean laysMultipleEggs() {
        return false;
    }

    public void setHasEgg(boolean b);
}
