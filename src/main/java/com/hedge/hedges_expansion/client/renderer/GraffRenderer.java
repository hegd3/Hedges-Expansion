package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.GraffModel;
import com.hedge.hedges_expansion.entity.living.GraffEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GraffRenderer extends MobRenderer<GraffEntity, GraffModel> {

    private static ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/graff.png");

    public GraffRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GraffModel(pContext.bakeLayer(EntityLayers.GRAFF_LAYER)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(GraffEntity pEntity) {
        return texture;
    }
}
