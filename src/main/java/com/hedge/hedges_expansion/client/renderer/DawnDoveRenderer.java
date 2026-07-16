package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.DawnDoveRiderLayer;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.DawnDoveModel;
import com.hedge.hedges_expansion.client.models.FerocetusModel;
import com.hedge.hedges_expansion.entity.living.DawnDoveEntity;
import com.hedge.hedges_expansion.entity.living.FerocetusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DawnDoveRenderer extends MobRenderer<DawnDoveEntity, DawnDoveModel> {

    private static final ResourceLocation texture = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/dawn_dove.png");


    public DawnDoveRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DawnDoveModel(pContext.bakeLayer(EntityLayers.DAWN_DOVE_LAYER)), 1.2f);
        this.addLayer(new DawnDoveRiderLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(DawnDoveEntity pEntity) {
        return texture;
    }

}
