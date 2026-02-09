package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.SkartleModel;
import com.hedge.hedges_expansion.entity.living.SkartleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkartleRenderer extends MobRenderer<SkartleEntity, SkartleModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/skartle.png");

    public SkartleRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SkartleModel(pContext.bakeLayer(EntityLayers.SKARTLE_LAYER)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SkartleEntity pEntity) {
        return texture;
    }
}
