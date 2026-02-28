package com.hedge.hedges_expansion.registry;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.entity.living.*;
import com.hedge.hedges_expansion.entity.living.ambientfish.GildGliderEntity;
import com.hedge.hedges_expansion.entity.living.ambientfish.GlimEntity;
import com.hedge.hedges_expansion.entity.living.ambientfish.SmarmEntity;
import com.hedge.hedges_expansion.entity.projectile.CorrosiveSpit;
import com.hedge.hedges_expansion.entity.projectile.MurkSmoke;
import com.hedge.hedges_expansion.entity.projectile.WaveEntity;
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

    public static final RegistryObject<EntityType<ZappetEntity>> ZAPPET =
            ENTITY_TYPES.register("zappet", () -> EntityType.Builder.of(ZappetEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.3f).build("zappet"));

    public static final RegistryObject<EntityType<GildGliderEntity>> GILD_GLIDER =
            ENTITY_TYPES.register("gild_glider", () -> EntityType.Builder.of(GildGliderEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 0.25f).build("gild_glider"));

    public static final RegistryObject<EntityType<SmarmEntity>> SMARM =
            ENTITY_TYPES.register("smarm", () -> EntityType.Builder.of(SmarmEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.6f, 0.14f).build("smarm"));


    public static final RegistryObject<EntityType<FerocetusEntity>> FEROCETUS =
            ENTITY_TYPES.register("ferocetus", () -> EntityType.Builder.of(FerocetusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(2.5f, 1.75f).build("ferocetus"));

    public static final RegistryObject<EntityType<WaveEntity>> WAVE =
            ENTITY_TYPES.register("wave", () -> EntityType.Builder.of(WaveEntity::new, MobCategory.MISC)
                    .sized(1.4f, 1.3f).build("wave"));

    public static final RegistryObject<EntityType<GlimEntity>> GLIM =
            ENTITY_TYPES.register("glim", () -> EntityType.Builder.of(GlimEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.625f, 0.375f).build("glim"));

    public static void register(IEventBus eventbus) {
        ENTITY_TYPES.register(eventbus);
    }

}
