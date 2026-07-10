package com.hedge.hedges_expansion.util;

public class HEColorUtils {
    public static int packColor(float r, float g, float b, float a) {
        int aInt = (int) (a * 255.0F) & 0xFF;
        int rInt = (int) (r * 255.0F) & 0xFF;
        int gInt = (int) (g * 255.0F) & 0xFF;
        int bInt = (int) (b * 255.0F) & 0xFF;
        return (aInt << 24) | (rInt << 16) | (gInt << 8) | bInt;
    }

    public static float unpackRed(int color) {
        return ((color >> 16) & 0xFF) / 255.0F;
    }

    public static float unpackGreen(int color) {
        return ((color >> 8) & 0xFF) / 255.0F;
    }

    public static float unpackBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    public static float unpackAlpha(int color) {
        return ((color >> 24) & 0xFF) / 255.0F;
    }
}
