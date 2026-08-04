package com.hedge.hedges_bestiary.registry;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.menu.HBTamableMenu;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HBMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, HedgesBestiary.MODID);


}
