package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.FerocetusModel;
import com.hedge.hedges_expansion.entity.living.FerocetusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FerocetusRenderer extends MobRenderer<FerocetusEntity, FerocetusModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/ferocetus.png");

    public FerocetusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new FerocetusModel(pContext.bakeLayer(EntityLayers.FEROCETUS_LAYER)), 1.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(FerocetusEntity pEntity) {
        return texture;
    }
}
