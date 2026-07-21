package com.hedge.hedges_bestiary.items;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.blocks.HEBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HBCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HedgesBestiary.MODID);


    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("hedges_bestiary_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(HBItems.MURK_CLAW.get()))
                    .title(Component.translatable("creativetab.hedges_bestiary_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // blocks
                        output.accept(HEBlocks.GURK_EGG.get());
                        output.accept(HEBlocks.ZAPPET_EGG.get());
                        output.accept(HEBlocks.DAWN_DOVE_EGG.get());
                        output.accept(HEBlocks.MURK_EGG.get());

                        // crafting items
                        output.accept(HBItems.MURK_CLAW.get());
                        output.accept(HBItems.MURK_SPIKE.get());
                        output.accept(HBItems.SCREAMER_SAC.get());
                        output.accept(HBItems.TEARACUDA_TOOTH.get());

                        //crafted items

                        output.accept(HBItems.PLAIN_TREAT.get());
                        output.accept(HBItems.SEASONED_TREAT.get());
                        output.accept(HBItems.HEARTY_TREAT.get());


                        // foods
                        output.accept(HBItems.RAW_URKMEAT.get());
                        output.accept(HBItems.COOKED_URKMEAT.get());

                        // buckets
                        output.accept(HBItems.GILD_GLIDER_BUCKET.get());
                        output.accept(HBItems.SMARM_BUCKET.get());

                        // spawn eggs
                        output.accept(HBItems.BURODON_SPAWN_EGG.get());
                        output.accept(HBItems.SPOTTED_STRIKER_SPAWN_EGG.get());
                        output.accept(HBItems.PLOMBO_SPAWN_EGG.get());
                        output.accept(HBItems.GURK_SPAWN_EGG.get());
                        output.accept(HBItems.MURK_SPAWN_EGG.get());
                        output.accept(HBItems.TEARACUDA_SPAWN_EGG.get());
                        output.accept(HBItems.ZAPPET_SPAWN_EGG.get());
                        output.accept(HBItems.GILD_GLIDER_SPAWN_EGG.get());
                        output.accept(HBItems.SMARM_SPAWN_EGG.get());
                        output.accept(HBItems.FEROCETUS_SPAWN_EGG.get());
                        output.accept(HBItems.GLIM_SPAWN_EGG.get());
                        output.accept(HBItems.BANSHEE_SPAWN_EGG.get());
                        output.accept(HBItems.DAWN_DOVE_SPAWN_EGG.get());


                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);

    }
}
