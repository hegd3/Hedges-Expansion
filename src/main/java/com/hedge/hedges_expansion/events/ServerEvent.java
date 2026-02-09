package com.hedge.hedges_expansion.events;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.particle.MurkChargeParticle;
import com.hedge.hedges_expansion.client.particle.ProjectileTrailParticle;
import com.hedge.hedges_expansion.client.particle.SmokeParticle;
import com.hedge.hedges_expansion.entity.living.*;
import com.hedge.hedges_expansion.registry.HEEntities;
import com.hedge.hedges_expansion.registry.HEParticles;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HedgesExpansion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ServerEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HEEntities.BURODON.get(), BurodonEntity.bakeAttributes().build());
        event.put(HEEntities.BEHEMOTH.get(), BehemothEntity.bakeAttributes().build());
        event.put(HEEntities.SPOTTED_STRIKER.get(), SpottedStrikerEntity.bakeAttributes().build());
        event.put(HEEntities.TRANSFIGURED.get(), TransfiguredEntity.bakeAttributes().build());
        event.put(HEEntities.GRUIN.get(), GruinEntity.bakeAttributes().build());
        event.put(HEEntities.GURK.get(), GurkEntity.bakeAttributes().build());
        event.put(HEEntities.BERG_BREAKER.get(), BergBreakerEntity.bakeAttributes().build());
        event.put(HEEntities.MURK.get(), MurkEntity.bakeAttributes().build());
        event.put(HEEntities.TEARACUDA.get(), TearacudaEntity.bakeAttributes().build());
        event.put(HEEntities.SKARTLE.get(), SkartleEntity.bakeAttributes().build());

    }

    @SubscribeEvent
    public static void entitySpawn(SpawnPlacementRegisterEvent event) {


    }


    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(HEParticles.MURK_CHARGE.get(), MurkChargeParticle.Provider::new);
        event.registerSpriteSet(HEParticles.MURK_CHARGE_SHOOT.get(), ProjectileTrailParticle.MurkChargeShotProvider::new);
        event.registerSpriteSet(HEParticles.SMOKE.get(), SmokeParticle.Provider::new);

    }
}
