package com.hedge.hedges_expansion.client.layer;

import com.hedge.hedges_expansion.HedgesExpansion;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class EntityLayers {
    public static final ModelLayerLocation BURODON_LAYER = main("burodon");
    public static final ModelLayerLocation SPOTTED_STRIKER_LAYER = main("spotted_striker");
    public static final ModelLayerLocation GRUIN_LAYER= main("gruin");
    public static final ModelLayerLocation GURK_LAYER= main("gurk");
    public static final ModelLayerLocation MURK_LAYER = main("murk");
    public static final ModelLayerLocation TEARACUDA_LAYER = main("tearacuda");
    public static final ModelLayerLocation ZAPPET_LAYER = main("zappet");
    public static final ModelLayerLocation GILD_GLIDER_LAYER = main("gild_glider");
    public static final ModelLayerLocation SMARM_LAYER = main("smarm");
    public static final ModelLayerLocation FEROCETUS_LAYER = main("ferocetus");
    public static final ModelLayerLocation WAVE_LAYER = main("wave");
    public static final ModelLayerLocation GLIM_LAYER = main("glim");
    public static final ModelLayerLocation GRAFF_LAYER = main("graff");
    public static final ModelLayerLocation SPEEL_LAYER = main("speel");

    public static final ModelLayerLocation GENERIC_PROJECTILE_LAYER = main("generic_projectile");

    private static ModelLayerLocation main(String id) {
        return new ModelLayerLocation(new ResourceLocation(HedgesExpansion.MODID, id), "main");
    }
}
