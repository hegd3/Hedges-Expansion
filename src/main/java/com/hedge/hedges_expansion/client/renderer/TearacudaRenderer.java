package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.layer.TearacudaEyeLayer;
import com.hedge.hedges_expansion.client.models.TearacudaModel;
import com.hedge.hedges_expansion.entity.living.TearacudaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TearacudaRenderer extends MobRenderer<TearacudaEntity, TearacudaModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/tearacuda/tearacuda.png");

    public TearacudaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TearacudaModel(pContext.bakeLayer(EntityLayers.TEARACUDA_LAYER)), 0.8f);
        this.addLayer(new TearacudaEyeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TearacudaEntity pEntity) {
        return texture;
    }
}
