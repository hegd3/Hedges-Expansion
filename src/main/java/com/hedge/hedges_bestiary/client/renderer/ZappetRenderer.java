package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.client.layer.ZappetGlowLayer;
import com.hedge.hedges_bestiary.client.models.ZappetModel;
import com.hedge.hedges_bestiary.entity.living.ZappetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZappetRenderer extends MobRenderer<ZappetEntity, ZappetModel> {
    private static final ResourceLocation texture = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/zappet/zappet.png");

    public ZappetRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ZappetModel(pContext.bakeLayer(EntityLayers.ZAPPET_LAYER)), 0.7f);
        this.addLayer(new ZappetGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ZappetEntity pEntity) {
        return texture;
    }
}
