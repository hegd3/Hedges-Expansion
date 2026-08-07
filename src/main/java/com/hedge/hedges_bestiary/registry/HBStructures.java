package com.hedge.hedges_bestiary.registry;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.worldgen.structure.DawnDoveNestStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class HBStructures {

    public static final DeferredRegister<StructureType<?>> DEF_REG = DeferredRegister.create(Registries.STRUCTURE_TYPE, HedgesBestiary.MODID);


    public static final RegistryObject<StructureType<DawnDoveNestStructure>> DAWN_DOVE_NEST = DEF_REG.register("dawn_dove_nest", () -> () -> DawnDoveNestStructure.CODEC);

    public static void register(IEventBus bus) {
        DEF_REG.register(bus);
    }
}

