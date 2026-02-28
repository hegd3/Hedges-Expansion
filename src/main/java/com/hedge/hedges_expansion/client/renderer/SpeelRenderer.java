package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.SpeelModel;
import com.hedge.hedges_expansion.entity.living.ambientfish.SpeelEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpeelRenderer extends MobRenderer<SpeelEntity, SpeelModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/speel.png");

    public SpeelRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SpeelModel(pContext.bakeLayer(EntityLayers.SPEEL_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpeelEntity pEntity) {
        return texture;
    }

}
