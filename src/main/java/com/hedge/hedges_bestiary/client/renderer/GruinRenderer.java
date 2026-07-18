package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.client.models.GruinModel;
import com.hedge.hedges_bestiary.entity.living.GruinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GruinRenderer extends MobRenderer<GruinEntity, GruinModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/gruin.png");

    public GruinRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GruinModel(pContext.bakeLayer(EntityLayers.GRUIN_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(GruinEntity pEntity) {
        return texture;
    }
}
