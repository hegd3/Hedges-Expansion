package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.client.ClientHelpers;
import com.hedge.hedges_expansion.client.layer.BehemothEyeLayer;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.BehemothModel;
import com.hedge.hedges_expansion.entity.living.BehemothEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BehemothRenderer extends MobRenderer<BehemothEntity, BehemothModel> {

    private static final ResourceLocation[] texture = ClientHelpers.generateVariants("behemoth/behemoth", 4);

    public BehemothRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BehemothModel(pContext.bakeLayer(EntityLayers.BEHEMOTH_LAYER)), 2.5f);
        this.addLayer(new BehemothEyeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BehemothEntity pEntity) {
        return texture[pEntity.getVariant()];
    }

}
