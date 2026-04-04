package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.BansheeGlowLayer;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.BansheeModel;
import com.hedge.hedges_expansion.entity.living.BansheeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BansheeRenderer extends MobRenderer<BansheeEntity, BansheeModel>  {
    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/banshee/banshee.png");

    public BansheeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BansheeModel(pContext.bakeLayer(EntityLayers.BANSHEE_LAYER)), 1.8f);
        this.addLayer(new BansheeGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BansheeEntity pEntity) {
        return texture;
    }
}
