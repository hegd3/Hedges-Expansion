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

    private static final ResourceLocation gurk_river = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/gurk/gurk_river.png");
    private static final ResourceLocation gurk_swamp = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/gurk/gurk_swamp.png");
    private static final ResourceLocation gurk_tropical = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/gurk/gurk_tropical.png");
    private static final ResourceLocation gurk_shiny = new ResourceLocation(HedgesExpansion.MODID, "textures/entity/gurk/gurk_shiny.png");

    public GurkRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GurkModel(pContext.bakeLayer(EntityLayers.GURK_LAYER)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation(GurkEntity pEntity) {
        return switch (pEntity.getVariant()) {
            case 1 -> gurk_swamp;
            case 2 -> gurk_tropical;
            case 3 -> gurk_shiny;
            default -> gurk_river;
        };
    }
}
