package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.BergBreakerModel;
import com.hedge.hedges_expansion.entity.living.BergBreakerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BergBreakerRenderer extends MobRenderer<BergBreakerEntity, BergBreakerModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/berg_breaker.png");

    public BergBreakerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BergBreakerModel(pContext.bakeLayer(EntityLayers.BERG_BREAKER_LAYER)), 1.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(BergBreakerEntity pEntity) {
        return texture;
    }
}
