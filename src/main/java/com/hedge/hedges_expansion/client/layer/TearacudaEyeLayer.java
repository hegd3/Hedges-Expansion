package com.hedge.hedges_expansion.client.layer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.models.TearacudaModel;
import com.hedge.hedges_expansion.entity.living.TearacudaEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class TearacudaEyeLayer extends EyesLayer<TearacudaEntity, TearacudaModel> {
    private static final RenderType texture = RenderType.eyes(new ResourceLocation(HedgesExpansion.MODID, "textures/entity/tearacuda/tearacuda_eyes.png"));

    public TearacudaEyeLayer(RenderLayerParent<TearacudaEntity, TearacudaModel> pRenderer) {
        super(pRenderer);
    }

    @Override
    public RenderType renderType() {
        return texture;
    }
}
