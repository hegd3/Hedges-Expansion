package com.hedge.hedges_bestiary.registry;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.entity.living.*;
import com.hedge.hedges_bestiary.entity.living.ambientfish.GildGliderEntity;
import com.hedge.hedges_bestiary.entity.living.ambientfish.ChubEntity;
import com.hedge.hedges_bestiary.entity.living.ambientfish.SkibEntity;
import com.hedge.hedges_bestiary.entity.projectile.EndgelScream;
import com.hedge.hedges_bestiary.entity.projectile.DawnDoveFireBall;
import com.hedge.hedges_bestiary.entity.projectile.MurkSmoke;
import com.hedge.hedges_bestiary.entity.projectile.WaveEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HBEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HedgesBestiary.MODID);

    public static final RegistryObject<EntityType<BurodonEntity>> BURODON =
            ENTITY_TYPES.register("burodon", () -> EntityType.Builder.of(BurodonEntity::new, MobCategory.CREATURE)
                    .sized(1.4f, 1.8f).build("burodon"));

    public static final RegistryObject<EntityType<SpottedStrikerEntity>> SPOTTED_STRIKER =
            ENTITY_TYPES.register("spotted_striker", () -> EntityType.Builder.of(SpottedStrikerEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.6f, 1.37f).build("spotted_striker"));

    public static final RegistryObject<EntityType<PlomboEntity>> PLOMBO =
            ENTITY_TYPES.register("plombo", () -> EntityType.Builder.of(PlomboEntity::new, MobCategory.CREATURE)
                    .sized(1.8f, 2.2f).build("plombo"));

    public static final RegistryObject<EntityType<GurkEntity>> GURK =
            ENTITY_TYPES.register("gurk", () -> EntityType.Builder.of(GurkEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 0.7f).build("gurk"));

    public static final RegistryObject<EntityType<MurkEntity>> MURK =
            ENTITY_TYPES.register("murk", () -> EntityType.Builder.of(MurkEntity::new, MobCategory.CREATURE)
                    .sized(2.45f, 2.18f).build("murk"));

    public static final RegistryObject<EntityType<MurkSmoke>> MURK_SMOKE =
            ENTITY_TYPES.register("murk_smoke", () -> EntityType.Builder.of(MurkSmoke::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("murk_smoke"));

    public static final RegistryObject<EntityType<TearacudaEntity>> TEARACUDA =
            ENTITY_TYPES.register("tearacuda", () -> EntityType.Builder.of(TearacudaEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.3f, 1f).build("tearacuda"));

    public static final RegistryObject<EntityType<ZappetEntity>> ZAPPET =
            ENTITY_TYPES.register("zappet", () -> EntityType.Builder.of(ZappetEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.3f).build("zappet"));

    public static final RegistryObject<EntityType<GildGliderEntity>> GILD_GLIDER =
            ENTITY_TYPES.register("gild_glider", () -> EntityType.Builder.of(GildGliderEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 0.25f).build("gild_glider"));

    public static final RegistryObject<EntityType<ChubEntity>> CHUB =
            ENTITY_TYPES.register("chub", () -> EntityType.Builder.of(ChubEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8f, 0.57f).build("chub"));

    public static final RegistryObject<EntityType<FerocetusEntity>> FEROCETUS =
            ENTITY_TYPES.register("ferocetus", () -> EntityType.Builder.of(FerocetusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(2.7f, 2.1f).build("ferocetus"));

    public static final RegistryObject<EntityType<WaveEntity>> WAVE =
            ENTITY_TYPES.register("wave", () -> EntityType.Builder.of(WaveEntity::new, MobCategory.MISC)
                    .sized(1.4f, 1.3f).build("wave"));

    public static final RegistryObject<EntityType<EndgelEntity>> ENDGEL =
            ENTITY_TYPES.register("endgel", () -> EntityType.Builder.of(EndgelEntity::new, MobCategory.MONSTER)
                    .sized(2.8f, 2.4f).build("endgel"));

    public static final RegistryObject<EntityType<EndgelScream>> BANSHEE_SCREAM =
            ENTITY_TYPES.register("banshee_scream", () -> EntityType.Builder.of(EndgelScream::new, MobCategory.MISC)
                    .sized(0.8f, 0.8f).build("banshee_scream"));

    public static final RegistryObject<EntityType<DawnDoveEntity>> DAWN_DOVE =
            ENTITY_TYPES.register("dawn_dove", () -> EntityType.Builder.of(DawnDoveEntity::new, MobCategory.CREATURE)
                    .sized(2.8f, 2.5f).build("dawn_dove"));

    public static final RegistryObject<EntityType<DawnDoveFireBall>> DAWN_DOVE_FIREBALL =
            ENTITY_TYPES.register("dawn_dove_fireball", () -> EntityType.Builder.of(DawnDoveFireBall::new, MobCategory.MISC)
                    .sized(1f, 1f).build("dawn_dove_fireball"));

    public static final RegistryObject<EntityType<SkibEntity>> SKIB =
            ENTITY_TYPES.register("skib", () -> EntityType.Builder.of(SkibEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.68f, 0.47f).build("skib"));
    public static void register(IEventBus eventbus) {
        ENTITY_TYPES.register(eventbus);
    }

}
