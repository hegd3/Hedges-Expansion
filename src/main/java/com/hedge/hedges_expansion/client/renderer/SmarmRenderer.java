package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.SmarmModel;
import com.hedge.hedges_expansion.entity.living.ambientfish.SmarmEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SmarmRenderer extends MobRenderer<SmarmEntity, SmarmModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/smarm.png");

    public SmarmRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SmarmModel(pContext.bakeLayer(EntityLayers.SMARM_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(SmarmEntity pEntity) {
        return texture;
    }
}
