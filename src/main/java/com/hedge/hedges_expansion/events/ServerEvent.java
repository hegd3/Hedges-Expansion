package com.hedge.hedges_expansion.events;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.particle.*;
import com.hedge.hedges_expansion.entity.living.*;
import com.hedge.hedges_expansion.entity.living.ambientfish.GildGliderEntity;
import com.hedge.hedges_expansion.entity.living.ambientfish.GlimEntity;
import com.hedge.hedges_expansion.entity.living.ambientfish.ChubEntity;
import com.hedge.hedges_expansion.registry.HEEntities;
import com.hedge.hedges_expansion.registry.HEParticles;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
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
        event.put(HEEntities.SPOTTED_STRIKER.get(), SpottedStrikerEntity.bakeAttributes().build());
        event.put(HEEntities.GRUIN.get(), GruinEntity.bakeAttributes().build());
        event.put(HEEntities.GURK.get(), GurkEntity.bakeAttributes().build());
        event.put(HEEntities.MURK.get(), MurkEntity.bakeAttributes().build());
        event.put(HEEntities.TEARACUDA.get(), TearacudaEntity.bakeAttributes().build());
        event.put(HEEntities.ZAPPET.get(), ZappetEntity.bakeAttributes().build());
        event.put(HEEntities.GILD_GLIDER.get(), GildGliderEntity.bakeAttributes().build());
        event.put(HEEntities.CHUB.get(), ChubEntity.bakeAttributes().build());
        event.put(HEEntities.FEROCETUS.get(), FerocetusEntity.bakeAttributes().build());
        event.put(HEEntities.GLIM.get(), GlimEntity.bakeAttributes().build());
        event.put(HEEntities.BANSHEE.get(), BansheeEntity.bakeAttributes().build());
        event.put(HEEntities.DAWN_DOVE.get(), DawnDoveEntity.bakeAttributes().build());

    }

    @SubscribeEvent
    public static void entitySpawn(SpawnPlacementRegisterEvent event) {
        event.register(HEEntities.GILD_GLIDER.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GildGliderEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HEEntities.CHUB.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChubEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HEEntities.TEARACUDA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TearacudaEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HEEntities.FEROCETUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FerocetusEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HEEntities.GLIM.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlimEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);

    }


    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(HEParticles.MURK_CHARGE.get(), MurkChargeParticle.Provider::new);
        event.registerSpriteSet(HEParticles.MURK_CHARGE_SHOOT.get(), ProjectileTrailParticle.MurkChargeShotProvider::new);
        event.registerSpriteSet(HEParticles.MURK_EXPLODE.get(), DynamicExplosionParticle.MurkExplosionProvider::new);
        event.registerSpriteSet(HEParticles.MURK_IMPACT.get(), DynamicExplosionParticle.MurkImpactProvider::new);

        event.registerSpriteSet(HEParticles.SMOKE.get(), SmokeParticle.Provider::new);
        event.registerSpriteSet(HEParticles.BANSHEE_SCREAM.get(), BansheeScreamParticle.Provider::new);

    }
}
