package com.hedge.hedges_bestiary;

import com.hedge.hedges_bestiary.blocks.HEBlocks;
import com.hedge.hedges_bestiary.client.ClientProxy;
import com.hedge.hedges_bestiary.client.HBSounds;
import com.hedge.hedges_bestiary.items.HBCreativeTab;
import com.hedge.hedges_bestiary.items.HBItems;
import com.hedge.hedges_bestiary.message.DanceJukeboxMessage;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBParticles;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import static org.antlr.runtime.debug.DebugEventListener.PROTOCOL_VERSION;

@Mod(HedgesBestiary.MODID)
public class HedgesBestiary
{
    public static final String MODID = "hedges_bestiary";

    private static final Logger LOGGER = LogUtils.getLogger();
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    private static final ResourceLocation PACKET_NETWORK_NAME = new ResourceLocation(MODID + ":main_channel");

    public static final SimpleChannel NETWORK_WRAPPER = NetworkRegistry.ChannelBuilder
            .named(PACKET_NETWORK_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public HedgesBestiary(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        HBEntities.register(modEventBus);
        HBItems.register(modEventBus);
        HEBlocks.registerBlocks(modEventBus);
        HBParticles.register(modEventBus);
        HBSounds.register(modEventBus);
        HBCreativeTab.register(modEventBus);
        PROXY.init();

    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        int packetsRegistered = 0;
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, DanceJukeboxMessage.class, DanceJukeboxMessage::write, DanceJukeboxMessage::read, DanceJukeboxMessage.Handler::handle);

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}
