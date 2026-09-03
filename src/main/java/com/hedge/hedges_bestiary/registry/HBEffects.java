package com.hedge.hedges_bestiary.registry;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.potion.VolatileEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HBEffects {
    public static final DeferredRegister<MobEffect> DEF_REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HedgesBestiary.MODID);
    public static final RegistryObject<MobEffect> VOLATILE = DEF_REG.register("volatile", VolatileEffect::new);

    public static void register(IEventBus bus) {
        DEF_REG.register(bus);
    }
}
