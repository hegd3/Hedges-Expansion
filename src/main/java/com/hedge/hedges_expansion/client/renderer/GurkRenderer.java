package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.ClientHelpers;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.GurkModel;
import com.hedge.hedges_expansion.entity.living.GurkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GurkRenderer extends MobRenderer<GurkEntity, GurkModel> {

    private static final ResourceLocation[] texture = ClientHelpers.generateVariants("gurk/gurk", 3);

    public GurkRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GurkModel(pContext.bakeLayer(EntityLayers.GURK_LAYER)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation(GurkEntity pEntity) {
        return texture[pEntity.getVariant()];
    }
}
