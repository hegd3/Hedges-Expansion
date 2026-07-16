package com.hedge.hedges_expansion.registry;

import com.hedge.hedges_expansion.HedgesExpansion;
import net.minecraft.client.KeyMapping;

import java.awt.event.KeyEvent;

public class HEKeyMappings {

    public static final KeyMapping MOUNT_ABILITY_KEY = create("mountSpecialKey", KeyEvent.VK_G);



    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + HedgesExpansion.MODID + "." + name, key, "key.category." + HedgesExpansion.MODID);
    }
}
