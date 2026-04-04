package com.hedge.hedges_expansion;

import com.hedge.hedges_expansion.client.HESounds;
import com.hedge.hedges_expansion.client.renderer.*;
import com.hedge.hedges_expansion.items.HECreativeTab;
import com.hedge.hedges_expansion.items.HEItems;
import com.hedge.hedges_expansion.registry.HEEntities;
import com.hedge.hedges_expansion.registry.HEParticles;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        HESounds.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}
