package com.hedge.hedges_bestiary.entity.util;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.function.Predicate;

public class CommonPredicates {

    public static final Predicate<ItemEntity> EATS_FISH = item -> item.getItem().is(ItemTags.FISHES);

}
