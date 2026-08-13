package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.client.models.PlomboModel;
import com.hedge.hedges_bestiary.entity.living.PlomboEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlomboRenderer extends MobRenderer<PlomboEntity, PlomboModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/plombo/plombo.png");
    private static final ResourceLocation SLEEPING = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/plombo/plombo_sleeping.png");

    public PlomboRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PlomboModel(pContext.bakeLayer(EntityLayers.PLOMBO_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(PlomboEntity pEntity) {
        return pEntity.isNapping() ? SLEEPING : TEXTURE;
    }
}
