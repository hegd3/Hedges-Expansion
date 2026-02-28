package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.layer.GlimGlowLayer;
import com.hedge.hedges_expansion.client.models.GlimModel;
import com.hedge.hedges_expansion.entity.living.ambientfish.GlimEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GlimRenderer extends MobRenderer<GlimEntity, GlimModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/glim/glim.png");


    public GlimRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GlimModel(pContext.bakeLayer(EntityLayers.GLIM_LAYER)), 0.3f);
        this.addLayer(new GlimGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(GlimEntity pEntity) {
        return texture;
    }
}
