package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.world.entity.Entity;

public interface KeybindUsingMount {

    void onKeyPacket(Entity keyPresser, int type);

}
