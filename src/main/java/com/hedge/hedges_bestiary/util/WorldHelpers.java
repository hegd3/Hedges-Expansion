package com.hedge.hedges_bestiary.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class WorldHelpers {

    public static BlockPos fromCoords(double x, double y, double z){
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public static BlockPos fromVec3(Vec3 vec3){
        return fromCoords(vec3.x, vec3.y, vec3.z);
    }

    public static Vec3 lerpVec3(double delta, Vec3 start, Vec3 end) {
        return new Vec3(Mth.lerp(delta, start.x, end.x), Mth.lerp(delta, start.y, end.y), Mth.lerp(delta, start.z, end.z));
    }
}
