package com.hedge.hedges_bestiary.events;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.particle.*;
import com.hedge.hedges_bestiary.entity.living.*;
import com.hedge.hedges_bestiary.entity.living.ambientfish.GildGliderEntity;
import com.hedge.hedges_bestiary.entity.living.ambientfish.ChubEntity;
import com.hedge.hedges_bestiary.entity.living.ambientfish.SkibEntity;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBParticles;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HedgesBestiary.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ServerEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HBEntities.BURODON.get(), BurodonEntity.bakeAttributes().build());
        event.put(HBEntities.SPOTTED_STRIKER.get(), SpottedStrikerEntity.bakeAttributes().build());
        event.put(HBEntities.PLOMBO.get(), PlomboEntity.bakeAttributes().build());
        event.put(HBEntities.GURK.get(), GurkEntity.bakeAttributes().build());
        event.put(HBEntities.MURK.get(), MurkEntity.bakeAttributes().build());
        event.put(HBEntities.TEARACUDA.get(), TearacudaEntity.bakeAttributes().build());
        event.put(HBEntities.ZAPPET.get(), ZappetEntity.bakeAttributes().build());
        event.put(HBEntities.GILD_GLIDER.get(), GildGliderEntity.bakeAttributes().build());
        event.put(HBEntities.CHUB.get(), ChubEntity.bakeAttributes().build());
        event.put(HBEntities.FEROCETUS.get(), FerocetusEntity.bakeAttributes().build());
        event.put(HBEntities.ENDGEL.get(), EndgelEntity.bakeAttributes().build());
        event.put(HBEntities.DAWN_DOVE.get(), DawnDoveEntity.bakeAttributes().build());
        event.put(HBEntities.SKIB.get(), SkibEntity.bakeAttributes().build());

    }

    @SubscribeEvent
    public static void entitySpawn(SpawnPlacementRegisterEvent event) {
        event.register(HBEntities.GILD_GLIDER.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GildGliderEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HBEntities.CHUB.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChubEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HBEntities.TEARACUDA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TearacudaEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(HBEntities.FEROCETUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FerocetusEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);

    }


    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(HBParticles.MURK_CHARGE.get(), AuraParticle.Provider::new);
        event.registerSpriteSet(HBParticles.MURK_CHARGE_SHOOT.get(), ProjectileTrailParticle.MurkChargeShotProvider::new);
        event.registerSpriteSet(HBParticles.MURK_EXPLODE.get(), DynamicExplosionParticle.MurkExplosionProvider::new);
        event.registerSpriteSet(HBParticles.MURK_IMPACT.get(), DynamicExplosionParticle.MurkImpactProvider::new);

        event.registerSpriteSet(HBParticles.SMOKE.get(), SmokeParticle.Provider::new);
        event.registerSpriteSet(HBParticles.BANSHEE_SCREAM.get(), BansheeScreamParticle.Provider::new);
        event.registerSpriteSet(HBParticles.ELECTRIC_SPARKS.get(), AuraParticle.Provider::new);
        event.registerSpriteSet(HBParticles.FIREBALL.get(), ProjectileTrailParticle.FireBallProvider::new);
        event.registerSpriteSet(HBParticles.SLEEP.get(), SleepParticle.Provider::new);

        event.registerSpriteSet(HBParticles.ENDGEL_TRAIL.get(), AuraParticle.Provider::new);
        event.registerSpriteSet(HBParticles.ENDGEL_EXPLODE.get(), DynamicExplosionParticle.EndgelExplodeProvider::new);
        event.registerSpriteSet(HBParticles.ENDGEL_BULLET.get(), ProjectileTrailParticle.MurkChargeShotProvider::new);

    }

}
