package com.hedge.hedges_expansion.registry;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.entity.living.*;
import com.hedge.hedges_expansion.entity.projectile.CorrosiveSpit;
import com.hedge.hedges_expansion.entity.projectile.MurkSmoke;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HEEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HedgesExpansion.MODID);

    public static final RegistryObject<EntityType<BurodonEntity>> BURODON =
            ENTITY_TYPES.register("burodon", () -> EntityType.Builder.of(BurodonEntity::new, MobCategory.CREATURE)
                    .sized(1.4f, 1.8f).build("burodon"));

    public static final RegistryObject<EntityType<BehemothEntity>> BEHEMOTH =
            ENTITY_TYPES.register("behemoth", () -> EntityType.Builder.of(BehemothEntity::new, MobCategory.MONSTER)
                    .sized(3f, 4f).build("behemoth"));

    public static final RegistryObject<EntityType<SpottedStrikerEntity>> SPOTTED_STRIKER =
            ENTITY_TYPES.register("spotted_striker", () -> EntityType.Builder.of(SpottedStrikerEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.3f, 1.2f).build("spotted_striker"));

    public static final RegistryObject<EntityType<TransfiguredEntity>> TRANSFIGURED =
            ENTITY_TYPES.register("transfigured", () -> EntityType.Builder.of(TransfiguredEntity::new, MobCategory.MONSTER)
                    .sized(1.7f, 3.8f).build("transfigured"));

    public static final RegistryObject<EntityType<GruinEntity>> GRUIN =
            ENTITY_TYPES.register("gruin", () -> EntityType.Builder.of(GruinEntity::new, MobCategory.CREATURE)
                    .sized(2.25f, 2.5f).build("gruin"));

    public static final RegistryObject<EntityType<GurkEntity>> GURK =
            ENTITY_TYPES.register("gurk", () -> EntityType.Builder.of(GurkEntity::new, MobCategory.CREATURE)
                    .sized(1.2f, 0.7f).build("gurk"));

    public static final RegistryObject<EntityType<BergBreakerEntity>> BERG_BREAKER =
            ENTITY_TYPES.register("berg_breaker", () -> EntityType.Builder.of(BergBreakerEntity::new, MobCategory.CREATURE)
                    .sized(2f, 1.5f).build("berg_breaker"));

    public static final RegistryObject<EntityType<MurkEntity>> MURK =
            ENTITY_TYPES.register("murk", () -> EntityType.Builder.of(MurkEntity::new, MobCategory.CREATURE)
                    .sized(2.45f, 2f).build("murk"));

    public static final RegistryObject<EntityType<MurkSmoke>> MURK_SMOKE =
            ENTITY_TYPES.register("murk_smoke", () -> EntityType.Builder.of(MurkSmoke::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("murk_smoke"));

    public static final RegistryObject<EntityType<TearacudaEntity>> TEARACUDA =
            ENTITY_TYPES.register("tearacuda", () -> EntityType.Builder.of(TearacudaEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.3f, 1f).build("tearacuda"));

    public static final RegistryObject<EntityType<SkartleEntity>> SKARTLE =
            ENTITY_TYPES.register("skartle", () -> EntityType.Builder.of(SkartleEntity::new, MobCategory.CREATURE)
                    .sized(1.7f, 2.3f).build("skartle"));

    public static final RegistryObject<EntityType<CorrosiveSpit>> CORROSIVE_SPIT =
            ENTITY_TYPES.register("corrosive_spit", () -> EntityType.Builder.of(CorrosiveSpit::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("corrosive_spit"));

    public static void register(IEventBus eventbus) {
        ENTITY_TYPES.register(eventbus);
    }

}
