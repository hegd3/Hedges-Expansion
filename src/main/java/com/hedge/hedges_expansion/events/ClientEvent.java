package com.hedge.hedges_expansion.events;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.ClientProxy;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.client.models.*;
import com.hedge.hedges_expansion.client.renderer.*;
import com.hedge.hedges_expansion.registry.HEEntities;
import com.hedge.hedges_expansion.registry.HEKeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = HedgesExpansion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientEvent {


    @SubscribeEvent
    public static void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        event.register(HEKeyMappings.MOUNT_ABILITY_KEY);
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
        EntityRenderers.register(HEEntities.BURODON.get(), BurodonRenderer::new);
        EntityRenderers.register(HEEntities.SPOTTED_STRIKER.get(), SpottedStrikerRenderer::new);
        EntityRenderers.register(HEEntities.GRUIN.get(), GruinRenderer::new);
        EntityRenderers.register(HEEntities.GURK.get(), GurkRenderer::new);
        EntityRenderers.register(HEEntities.MURK.get(), MurkRenderer::new);
        EntityRenderers.register(HEEntities.MURK_SMOKE.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HEEntities.TEARACUDA.get(), TearacudaRenderer::new);
        EntityRenderers.register(HEEntities.ZAPPET.get(), ZappetRenderer::new);
        EntityRenderers.register(HEEntities.GILD_GLIDER.get(), GildGliderRenderer::new);
        EntityRenderers.register(HEEntities.CHUB.get(), ChubRenderer::new);
        EntityRenderers.register(HEEntities.FEROCETUS.get(), FerocetusRenderer::new);
        EntityRenderers.register(HEEntities.WAVE.get(), WaveRenderer::new);
        EntityRenderers.register(HEEntities.GLIM.get(), GlimRenderer::new);
        EntityRenderers.register(HEEntities.BANSHEE.get(), BansheeRenderer::new);
        EntityRenderers.register(HEEntities.BANSHEE_SCREAM.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HEEntities.DAWN_DOVE.get(), DawnDoveRenderer::new);

    }

}