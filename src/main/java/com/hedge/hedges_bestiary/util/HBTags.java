package com.hedge.hedges_bestiary.util;

import com.hedge.hedges_bestiary.HedgesBestiary;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;


public class HBTags {

    public static class Biomes {
        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(HedgesBestiary.MODID, name));
        }
    }

    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(HedgesBestiary.MODID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> PLAIN_TREAT_INGREDIENT = tag("plain_treat_ingredient");
        public static final TagKey<Item> SEASONED_TREAT_INGREDIENT = tag("seasoneed_treat_ingredient");
        public static final TagKey<Item> HEARTY_TREAT_INGREDIENT = tag("hearty_treat_ingredient");


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(HedgesBestiary.MODID, name));
        }
    }
}
