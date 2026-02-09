package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.BurodonModel;
import com.hedge.hedges_expansion.entity.living.BurodonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BurodonRenderer extends MobRenderer<BurodonEntity, BurodonModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/burodon.png");

    public BurodonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BurodonModel(pContext.bakeLayer(EntityLayers.BURODON_LAYER)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(BurodonEntity pEntity) {
        return texture;
    }
}
