package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.SpottedStrikerModel;
import com.hedge.hedges_expansion.entity.living.SpottedStrikerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpottedStrikerRenderer extends MobRenderer<SpottedStrikerEntity, SpottedStrikerModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/spotted_striker.png");

    public SpottedStrikerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SpottedStrikerModel(pContext.bakeLayer(EntityLayers.SPOTTED_STRIKER_LAYER)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpottedStrikerEntity pEntity) {
        return texture;
    }
}
