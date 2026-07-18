package com.hedge.hedges_bestiary.events;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.client.models.*;
import com.hedge.hedges_bestiary.client.renderer.*;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBKeyMappings;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = HedgesBestiary.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientEvent {


    @SubscribeEvent
    public static void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        event.register(HBKeyMappings.MOUNT_ABILITY_KEY);
    }


    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EntityLayers.BURODON_LAYER, BurodonModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.SPOTTED_STRIKER_LAYER, SpottedStrikerModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GRUIN_LAYER, GruinModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GURK_LAYER, GurkModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.MURK_LAYER, MurkModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.TEARACUDA_LAYER, TearacudaModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.ZAPPET_LAYER, ZappetModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GILD_GLIDER_LAYER, GildGliderModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.CHUB_LAYER, ChubModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.FEROCETUS_LAYER, FerocetusModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.WAVE_LAYER, WaveModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GLIM_LAYER, GlimModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.BANSHEE_LAYER, BansheeModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.DAWN_DOVE_LAYER, DawnDoveModel::createBodyLayer);

    }

    @SubscribeEvent
    public static void registerRenderer(FMLClientSetupEvent event)
    {
        EntityRenderers.register(HBEntities.BURODON.get(), BurodonRenderer::new);
        EntityRenderers.register(HBEntities.SPOTTED_STRIKER.get(), SpottedStrikerRenderer::new);
        EntityRenderers.register(HBEntities.GRUIN.get(), GruinRenderer::new);
        EntityRenderers.register(HBEntities.GURK.get(), GurkRenderer::new);
        EntityRenderers.register(HBEntities.MURK.get(), MurkRenderer::new);
        EntityRenderers.register(HBEntities.MURK_SMOKE.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HBEntities.TEARACUDA.get(), TearacudaRenderer::new);
        EntityRenderers.register(HBEntities.ZAPPET.get(), ZappetRenderer::new);
        EntityRenderers.register(HBEntities.GILD_GLIDER.get(), GildGliderRenderer::new);
        EntityRenderers.register(HBEntities.CHUB.get(), ChubRenderer::new);
        EntityRenderers.register(HBEntities.FEROCETUS.get(), FerocetusRenderer::new);
        EntityRenderers.register(HBEntities.WAVE.get(), WaveRenderer::new);
        EntityRenderers.register(HBEntities.GLIM.get(), GlimRenderer::new);
        EntityRenderers.register(HBEntities.BANSHEE.get(), BansheeRenderer::new);
        EntityRenderers.register(HBEntities.BANSHEE_SCREAM.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HBEntities.DAWN_DOVE.get(), DawnDoveRenderer::new);

    }

}