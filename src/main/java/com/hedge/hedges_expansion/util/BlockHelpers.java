package com.hedge.hedges_expansion.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockHelpers {

    public static VoxelShape createRectangular(int widthPx, int heightPx) {
        int px = (16 - widthPx) / 2;
        return Block.box(px, 0, px, 16 - px, heightPx, 16 - px);
    }
}
