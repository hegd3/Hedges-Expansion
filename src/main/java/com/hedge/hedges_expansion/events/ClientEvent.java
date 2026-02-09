package com.hedge.hedges_expansion.events;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.*;
import com.hedge.hedges_expansion.client.renderer.BehemothRenderer;
import com.hedge.hedges_expansion.client.renderer.BergBreakerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = HedgesExpansion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientEvent {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EntityLayers.BURODON_LAYER, BurodonModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.BEHEMOTH_LAYER, BehemothModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.SPOTTED_STRIKER_LAYER, SpottedStrikerModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.TRANSFIGURED_LAYER, TransfiguredModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GRUIN_LAYER, GruinModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GURK_LAYER, GurkModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.BERG_BREAKER_LAYER, BergBreakerModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.MURK_LAYER, MurkModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.TEARACUDA_LAYER, TearacudaModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.SKARTLE_LAYER, SkartleModel::createBodyLayer);

    }
}