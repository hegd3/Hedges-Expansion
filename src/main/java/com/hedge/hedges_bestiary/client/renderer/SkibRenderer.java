package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.client.models.SkibModel;
import com.hedge.hedges_bestiary.entity.living.ambientfish.SkibEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkibRenderer extends MobRenderer<SkibEntity, SkibModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/skib/skib.png");

    public SkibRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SkibModel(pContext.bakeLayer(EntityLayers.SKIB_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(SkibEntity entity) {
        return TEXTURE;
    }
}
