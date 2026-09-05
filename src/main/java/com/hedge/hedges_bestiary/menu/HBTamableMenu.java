package com.hedge.hedges_bestiary.menu;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HBTamableMenu extends AbstractContainerMenu {
    private final HBTamableAnimal animal;

    public HBTamableMenu(int pContainerId, HBTamableAnimal animal) {
        super(null, pContainerId);
        this.animal = animal;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.animal.isAlive() && this.animal.distanceTo(player) < 8.0f;
    }
}
