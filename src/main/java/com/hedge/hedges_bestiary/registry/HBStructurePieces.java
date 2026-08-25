package com.hedge.hedges_bestiary.registry;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.worldgen.structure.DawnDoveNestStructure;
import com.hedge.hedges_bestiary.worldgen.structure.piece.DawnDoveNestPiece;
import com.hedge.hedges_bestiary.worldgen.structure.piece.PlomboTerritoryPiece;
import com.hedge.hedges_bestiary.worldgen.structure.piece.ZappetRoostPiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HBStructurePieces {

    public static final DeferredRegister<StructurePieceType> DEF_REG = DeferredRegister.create(Registries.STRUCTURE_PIECE, HedgesBestiary.MODID);


    public static final RegistryObject<StructurePieceType> DAWN_DOVE_NEST = DEF_REG.register("dawn_dove_nest", () -> DawnDoveNestPiece::new);

    public static final RegistryObject<StructurePieceType> ZAPPET_ROOST = DEF_REG.register("zappet_roost", () -> ZappetRoostPiece::new);

    public static final RegistryObject<StructurePieceType> PLOMBO_TERRITORY = DEF_REG.register("plombo_territory", () -> PlomboTerritoryPiece::new);

    public static void register(IEventBus eventBus) {
        DEF_REG.register(eventBus);
    }

}
