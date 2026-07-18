package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.client.layer.MurkEyeLayer;
import com.hedge.hedges_bestiary.client.models.MurkModel;
import com.hedge.hedges_bestiary.entity.living.MurkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MurkRenderer extends MobRenderer<MurkEntity, MurkModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/murk/murk.png");


    public MurkRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MurkModel(pContext.bakeLayer(EntityLayers.MURK_LAYER)), 2.0f);
        this.addLayer(new MurkEyeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MurkEntity pEntity) {
        return texture;
    }
}
