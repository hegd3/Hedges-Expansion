package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.GruinModel;
import com.hedge.hedges_expansion.entity.living.GruinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GruinRenderer extends MobRenderer<GruinEntity, GruinModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/gruin.png");

    public GruinRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GruinModel(pContext.bakeLayer(EntityLayers.GRUIN_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(GruinEntity pEntity) {
        return texture;
    }
}
