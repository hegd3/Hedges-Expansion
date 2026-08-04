package com.hedge.hedges_bestiary.data.datagen.providers;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.blocks.HBBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class HBItemModelProvider extends ItemModelProvider {
    public HBItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HedgesBestiary.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlock(HBBlocks.GURK_EGG);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HedgesBestiary.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlock(RegistryObject<Block> block) {
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HedgesBestiary.MODID, "item/" + block.getId().getPath())
                );

    }
}
