package com.hedge.hedges_bestiary.registry;

import com.hedge.hedges_bestiary.HedgesBestiary;
import net.minecraft.client.KeyMapping;

import java.awt.event.KeyEvent;

public class HBKeyMappings {

    public static final KeyMapping MOUNT_ABILITY_KEY = create("mountSpecialKey", KeyEvent.VK_G);



    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + HedgesBestiary.MODID + "." + name, key, "key.category." + HedgesBestiary.MODID);
    }
}
