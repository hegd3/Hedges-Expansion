package com.hedge.hedges_expansion.entity.types;

import net.minecraft.world.entity.Entity;

public interface KeybindUsingMount {

    void onKeyPacket(Entity keyPresser, int type);

}
