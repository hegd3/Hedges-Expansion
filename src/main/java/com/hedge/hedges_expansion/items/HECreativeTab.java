package com.hedge.hedges_expansion.items;

import com.hedge.hedges_expansion.HedgesExpansion;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HECreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HedgesExpansion.MODID);


    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("hedges_expansion_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(HEItems.MURK_CLAW.get()))
                    .title(Component.translatable("creativetab.hedges_expansion_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        // crafting items
                        output.accept(HEItems.MURK_CLAW.get());
                        output.accept(HEItems.MURK_SPIKE.get());
                        output.accept(HEItems.TEARACUDA_TOOTH.get());

                        // foods
                        output.accept(HEItems.RAW_URKMEAT.get());
                        output.accept(HEItems.COOKED_URKMEAT.get());

                        // buckets
                        output.accept(HEItems.GILD_GLIDER_BUCKET.get());
                        output.accept(HEItems.SMARM_BUCKET.get());

                        // spawn eggs
                        output.accept(HEItems.BURODON_SPAWN_EGG.get());
                        output.accept(HEItems.SPOTTED_STRIKER_SPAWN_EGG.get());
                        output.accept(HEItems.GRUIN_SPAWN_EGG.get());
                        output.accept(HEItems.GURK_SPAWN_EGG.get());
                        output.accept(HEItems.MURK_SPAWN_EGG.get());
                        output.accept(HEItems.TEARACUDA_SPAWN_EGG.get());
                        output.accept(HEItems.ZAPPET_SPAWN_EGG.get());
                        output.accept(HEItems.GILD_GLIDER_SPAWN_EGG.get());
                        output.accept(HEItems.SMARM_SPAWN_EGG.get());
                        output.accept(HEItems.FEROCETUS_SPAWN_EGG.get());
                        output.accept(HEItems.GLIM_SPAWN_EGG.get());
                        output.accept(HEItems.GRAFF_SPAWN_EGG.get());
                        output.accept(HEItems.SPEEL_SPAWN_EGG.get());
                        output.accept(HEItems.BANSHEE_SPAWN_EGG.get());



                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);

    }
}
