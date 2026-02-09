package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.layer.MurkEyeLayer;
import com.hedge.hedges_expansion.client.models.MurkModel;
import com.hedge.hedges_expansion.entity.living.MurkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MurkRenderer extends MobRenderer<MurkEntity, MurkModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/murk/murk.png");


    public MurkRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MurkModel(pContext.bakeLayer(EntityLayers.MURK_LAYER)), 2.0f);
        this.addLayer(new MurkEyeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MurkEntity pEntity) {
        return texture;
    }
}
