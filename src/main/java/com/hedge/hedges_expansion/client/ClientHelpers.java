package com.hedge.hedges_expansion.client;

import com.hedge.hedges_expansion.HedgesExpansion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;

public class ClientHelpers {


    public static ResourceLocation[] generateVariants(String path, int count) {
        ResourceLocation[] textures = new ResourceLocation[count];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/" + path + "_" + i + ".png");
        }
        return textures;
    }

    public static RenderType[] generateEyeVariants(String path, int count) {
        RenderType[] textures = new RenderType[count];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = RenderType.eyes(new ResourceLocation(HedgesExpansion.MODID, "textures/entity/" + path + "_" + i + ".png"));
        }
        return textures;
    }

}

