package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.ChubModel;
import com.hedge.hedges_expansion.entity.living.ambientfish.ChubEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChubRenderer extends MobRenderer<ChubEntity, ChubModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/chub.png");

    public ChubRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ChubModel(pContext.bakeLayer(EntityLayers.CHUB_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(ChubEntity pEntity) {
        return texture;
    }
}
