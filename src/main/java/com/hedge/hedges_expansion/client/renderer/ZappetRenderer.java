package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.ZappetModel;
import com.hedge.hedges_expansion.entity.living.ZappetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZappetRenderer extends MobRenderer<ZappetEntity, ZappetModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/zappet.png");

    public ZappetRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ZappetModel(pContext.bakeLayer(EntityLayers.ZAPPET_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZappetEntity pEntity) {
        return texture;
    }
}
