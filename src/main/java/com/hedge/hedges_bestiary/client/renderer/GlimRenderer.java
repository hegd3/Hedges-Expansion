package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.client.layer.GlimGlowLayer;
import com.hedge.hedges_bestiary.client.models.GlimModel;
import com.hedge.hedges_bestiary.entity.living.ambientfish.GlimEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GlimRenderer extends MobRenderer<GlimEntity, GlimModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/glim/glim.png");


    public GlimRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GlimModel(pContext.bakeLayer(EntityLayers.GLIM_LAYER)), 0.3f);
        this.addLayer(new GlimGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(GlimEntity pEntity) {
        return texture;
    }
}
