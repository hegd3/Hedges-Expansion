package com.hedge.hedges_expansion;

import com.hedge.hedges_expansion.client.renderer.*;
import com.hedge.hedges_expansion.items.HECreativeTab;
import com.hedge.hedges_expansion.items.HEItems;
import com.hedge.hedges_expansion.registry.HEEntities;
import com.hedge.hedges_expansion.registry.HEParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HedgesExpansion.MODID)
public class HedgesExpansion
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "hedges_expansion";

    private static final Logger LOGGER = LogUtils.getLogger();

    public HedgesExpansion(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        HEEntities.register(modEventBus);
        HECreativeTab.register(modEventBus);
        HEItems.register(modEventBus);
        HEParticles.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(HEEntities.BURODON.get(), BurodonRenderer::new);
            EntityRenderers.register(HEEntities.BEHEMOTH.get(), BehemothRenderer::new);
            EntityRenderers.register(HEEntities.SPOTTED_STRIKER.get(), SpottedStrikerRenderer::new);
            EntityRenderers.register(HEEntities.TRANSFIGURED.get(), TransfiguredRenderer::new);
            EntityRenderers.register(HEEntities.GRUIN.get(), GruinRenderer::new);
            EntityRenderers.register(HEEntities.GURK.get(), GurkRenderer::new);
            EntityRenderers.register(HEEntities.BERG_BREAKER.get(), BergBreakerRenderer::new);
            EntityRenderers.register(HEEntities.MURK.get(), MurkRenderer::new);
            EntityRenderers.register(HEEntities.MURK_SMOKE.get(), ModellessProjectileRenderer::new);
            EntityRenderers.register(HEEntities.TEARACUDA.get(), TearacudaRenderer::new);
            EntityRenderers.register(HEEntities.SKARTLE.get(), SkartleRenderer::new);
            EntityRenderers.register(HEEntities.CORROSIVE_SPIT.get(), ModellessProjectileRenderer::new);

        }
    }
}
