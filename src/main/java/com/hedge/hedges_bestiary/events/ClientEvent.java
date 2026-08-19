package com.hedge.hedges_bestiary.events;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.client.models.*;
import com.hedge.hedges_bestiary.client.renderer.*;
import com.hedge.hedges_bestiary.client.renderer.projectile.WaveRenderer;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.menu.HBTamableMenu;
import com.hedge.hedges_bestiary.menu.HBTamableMenuScreen;
import com.hedge.hedges_bestiary.message.OpenTamableScreenMessage;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Optional;

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
        event.registerLayerDefinition(EntityLayers.PLOMBO_LAYER, PlomboModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GURK_LAYER, GurkModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.MURK_LAYER, MurkModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.TEARACUDA_LAYER, TearacudaModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.ZAPPET_LAYER, ZappetModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GILD_GLIDER_LAYER, GildGliderModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.CHUB_LAYER, ChubModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.FEROCETUS_LAYER, FerocetusModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.WAVE_LAYER, WaveModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.BANSHEE_LAYER, EndgelModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.DAWN_DOVE_LAYER, DawnDoveModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.SKIB_LAYER, SkibModel::createBodyLayer);
        event.registerLayerDefinition(EntityLayers.GENERIC_PROJECTILE_LAYER, CrossedProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderer(FMLClientSetupEvent event)
    {
        EntityRenderers.register(HBEntities.BURODON.get(), BurodonRenderer::new);
        EntityRenderers.register(HBEntities.SPOTTED_STRIKER.get(), SpottedStrikerRenderer::new);
        EntityRenderers.register(HBEntities.PLOMBO.get(), PlomboRenderer::new);
        EntityRenderers.register(HBEntities.GURK.get(), GurkRenderer::new);
        EntityRenderers.register(HBEntities.MURK.get(), MurkRenderer::new);
        EntityRenderers.register(HBEntities.MURK_SMOKE.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HBEntities.TEARACUDA.get(), TearacudaRenderer::new);
        EntityRenderers.register(HBEntities.ZAPPET.get(), ZappetRenderer::new);
        EntityRenderers.register(HBEntities.GILD_GLIDER.get(), GildGliderRenderer::new);
        EntityRenderers.register(HBEntities.CHUB.get(), ChubRenderer::new);
        EntityRenderers.register(HBEntities.FEROCETUS.get(), FerocetusRenderer::new);
        EntityRenderers.register(HBEntities.WAVE.get(), WaveRenderer::new);
        EntityRenderers.register(HBEntities.ENDGEL.get(), EndgelRenderer::new);
        EntityRenderers.register(HBEntities.ENDGEL_BULLET.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HBEntities.DAWN_DOVE.get(), DawnDoveRenderer::new);
        EntityRenderers.register(HBEntities.DRAGON_FIREBALL.get(), ModellessProjectileRenderer::new);
        EntityRenderers.register(HBEntities.SKIB.get(), SkibRenderer::new);

        // MenuScreens.register();
    }

    public static void openTamableScreen(OpenTamableScreenMessage packet) {
        Minecraft client = Minecraft.getInstance();
        Level level = client.level;
        Optional.ofNullable(level).ifPresent(world -> {
            Entity entity = world.getEntity(packet.getId());
            if (entity instanceof HBTamableAnimal animal) {
                int syncId = packet.getSyncId();
                LocalPlayer clientPlayerEntity = client.player;
                assert clientPlayerEntity != null;
                HBTamableMenu menu = new HBTamableMenu(syncId, animal);
                clientPlayerEntity.containerMenu = menu;
                client.execute(() -> client.setScreen(new HBTamableMenuScreen(menu, clientPlayerEntity.getInventory(), animal)));
            }
        });
    }

}