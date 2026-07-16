package com.hedge.hedges_expansion.blocks;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.items.HEItems;
import com.hedge.hedges_expansion.registry.HEEntities;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class HEBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HedgesExpansion.MODID);

    public static final RegistryObject<Block> MURK_EGG = registerBlock("murk_egg",
            () -> new EggBlock(BlockBehaviour.Properties.copy(Blocks.SNIFFER_EGG).randomTicks(),
                    HEEntities.MURK, BlockTags.SAND, EggBlock.LARGE_EGG));

    public static final RegistryObject<Block> DAWN_DOVE_EGG = registerBlock("dawn_dove_egg",
            () -> new EggBlock(BlockBehaviour.Properties.copy(Blocks.SNIFFER_EGG).randomTicks(),
                    HEEntities.DAWN_DOVE, BlockTags.TERRACOTTA, EggBlock.LARGE_EGG));


    private static <T extends Block> Supplier<T> create(String key, Supplier<T> block) {
        return BLOCKS.register(key, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name,RegistryObject<T> block){
        return HEItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void registerBlocks(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
