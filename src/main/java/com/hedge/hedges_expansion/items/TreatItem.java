package com.hedge.hedges_expansion.items;

import net.minecraft.world.item.Item;

public class TreatItem extends Item {

    private final int tier;
    public TreatItem(int tier) {
        super(new Item.Properties());
        this.tier = tier;
    }

    public int getTier() {
        return this.tier;
    }
}
