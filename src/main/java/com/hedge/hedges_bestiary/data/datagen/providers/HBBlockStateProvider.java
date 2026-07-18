package com.hedge.hedges_bestiary.data.datagen.providers;

import com.hedge.hedges_bestiary.HedgesBestiary;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class HBBlockStateProvider extends BlockStateProvider {
    public HBBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HedgesBestiary.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    private void blockWithItem(RegistryObject<Block> block){
        simpleBlockWithItem(block.get(),
                cubeAll(block.get()));
    }

}
