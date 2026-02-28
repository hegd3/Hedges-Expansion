package com.hedge.hedges_expansion.items;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.registry.HEEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class HEItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HedgesExpansion.MODID);


    public static final RegistryObject<Item> HYDROVSLER_CLAW = ITEMS.register("hydrovsler_claw",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BURODON_SPAWN_EGG = ITEMS.register("burodon_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.BURODON, 0xAEC8D4, 0x7E83CC, new Item.Properties()));

    public static final RegistryObject<Item> SPOTTED_STRIKER_SPAWN_EGG = ITEMS.register("spotted_striker_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.SPOTTED_STRIKER, 0x50555E, 0x8A896B, new Item.Properties()));

    public static final RegistryObject<Item> TRANSFIGURED_SPAWN_EGG = ITEMS.register("transfigured_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.TRANSFIGURED, 0x827258, 0x8A7465, new Item.Properties()));

    public static final RegistryObject<Item> GRUIN_SPAWN_EGG = ITEMS.register("gruin_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.GRUIN, 0x21152B, 0x968466, new Item.Properties()));

    public static final RegistryObject<Item> GURK_SPAWN_EGG = ITEMS.register("gurk_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.GURK, 0x2E7037, 0x63A64E, new Item.Properties()));

    public static final RegistryObject<Item> MURK_SPAWN_EGG = ITEMS.register("murk_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.MURK, 0x473B8A, 0xC4BE84, new Item.Properties()));

    public static final RegistryObject<Item> TEARACUDA_SPAWN_EGG = ITEMS.register("tearacuda_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.TEARACUDA, 0x6898D4, 0xD4BB68, new Item.Properties()));

    public static final RegistryObject<Item> SKARTLE_SPAWN_EGG = ITEMS.register("skartle_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.SKARTLE, 0x807D74, 0x1F1E2E, new Item.Properties()));

    public static final RegistryObject<Item> ZAPPET_SPAWN_EGG = createEgg("zappet", HEEntities.ZAPPET, 0xE38E66, 0x88ACE3);

    public static final RegistryObject<Item> GILD_GLIDER_SPAWN_EGG = createEgg("gild_glider", HEEntities.GILD_GLIDER, 0xE3D188, 0xDED0B1);

    public static final RegistryObject<Item> SMARM_SPAWN_EGG = createEgg("smarm", HEEntities.SMARM, 0x53A6C9, 0xC9536B);

    public static final RegistryObject<Item> FEROCETUS_SPAWN_EGG = createEgg("ferocetus", HEEntities.FEROCETUS, 0x75604C, 0xC7A169);

    public static final RegistryObject<Item> GLIM_SPAWN_EGG = createEgg("glim", HEEntities.GLIM, 0x252329, 0xBD9E73);

    public static final RegistryObject<Item> GRAFF_SPAWN_EGG = createEgg("graff", HEEntities.GRAFF, 0xAB776A, 0x88A4BA);

    public static final RegistryObject<Item> SPEEL_SPAWN_EGG = createEgg("speel", HEEntities.SPEEL, 0x467599, 0xB8C6D1);


    public static final RegistryObject<Item> GILD_GLIDER_BUCKET = createBucket("gild_glider", HEEntities.GILD_GLIDER);

    public static final RegistryObject<Item> SMARM_BUCKET = createBucket("smarm", HEEntities.SMARM);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> createEgg(String name, Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor) {
        return ITEMS.register(name + "_spawn_egg",
                () -> new ForgeSpawnEggItem(type, backgroundColor, highlightColor, new Item.Properties()));
    }

    private static RegistryObject<Item> createBucket(String name, Supplier<? extends EntityType<?>> type) {
        return ITEMS.register(name + "_bucket",
                () -> new WaterMobBucketItem(type));
    }

}
