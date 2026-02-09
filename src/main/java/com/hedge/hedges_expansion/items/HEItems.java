package com.hedge.hedges_expansion.items;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.registry.HEEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HEItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HedgesExpansion.MODID);


    public static final RegistryObject<Item> HYDROVSLER_CLAW = ITEMS.register("hydrovsler_claw",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BURODON_SPAWN_EGG = ITEMS.register("burodon_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.BURODON, 0xAEC8D4, 0x7E83CC, new Item.Properties()));

    public static final RegistryObject<Item> BEHEMOTH_SPAWN_EGG = ITEMS.register("behemoth_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.BEHEMOTH, 0x3F3347, 0x454263, new Item.Properties()));

    public static final RegistryObject<Item> SPOTTED_STRIKER_SPAWN_EGG = ITEMS.register("spotted_striker_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.SPOTTED_STRIKER, 0x50555E, 0x8A896B, new Item.Properties()));

    public static final RegistryObject<Item> TRANSFIGURED_SPAWN_EGG = ITEMS.register("transfigured_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.TRANSFIGURED, 0x827258, 0x8A7465, new Item.Properties()));

    public static final RegistryObject<Item> GRUIN_SPAWN_EGG = ITEMS.register("gruin_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.GRUIN, 0x21152B, 0x968466, new Item.Properties()));

    public static final RegistryObject<Item> GURK_SPAWN_EGG = ITEMS.register("gurk_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.GURK, 0x2E7037, 0x63A64E, new Item.Properties()));

    public static final RegistryObject<Item> BERG_BREAKER_SPAWN_EGG = ITEMS.register("berg_breaker_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.BERG_BREAKER, 0xB2C2D1, 0xD5E3F0, new Item.Properties()));

    public static final RegistryObject<Item> MURK_SPAWN_EGG = ITEMS.register("murk_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.MURK, 0x473B8A, 0xC4BE84, new Item.Properties()));

    public static final RegistryObject<Item> TEARACUDA_SPAWN_EGG = ITEMS.register("tearacuda_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.TEARACUDA, 0x6898D4, 0xD4BB68, new Item.Properties()));

    public static final RegistryObject<Item> SKARTLE_SPAWN_EGG = ITEMS.register("skartle_spawn_egg",
            () -> new ForgeSpawnEggItem(HEEntities.SKARTLE, 0x807D74, 0x1F1E2E, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
