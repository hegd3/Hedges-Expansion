package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.client.ClientHelpers;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.layer.TransfiguredEyeLayer;
import com.hedge.hedges_expansion.client.models.TransfiguredModel;
import com.hedge.hedges_expansion.entity.living.TransfiguredEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TransfiguredRenderer extends MobRenderer<TransfiguredEntity, TransfiguredModel> {

    private static ResourceLocation[] texture = ClientHelpers.generateVariants("transfigured/transfigured", 3);

    public TransfiguredRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TransfiguredModel(pContext.bakeLayer(EntityLayers.TRANSFIGURED_LAYER)), 1.3f);
        this.addLayer(new TransfiguredEyeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TransfiguredEntity pEntity) {
        return texture[pEntity.mouthProgress];
    }


}
