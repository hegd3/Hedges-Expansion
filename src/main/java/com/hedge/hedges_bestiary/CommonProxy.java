package com.hedge.hedges_bestiary;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = HedgesBestiary.MODID, bus =  Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    public void init() {

    }


    public void handleJukeboxPacket(Level level, int entityId, BlockPos jukeBox, boolean dancing) {
        Entity entity = level.getEntity(entityId);
        if (entity instanceof HBTamableAnimal dancer && dancer.isSitting() && dancer.getTarget() == null) {
            dancer.setDancing(dancing);
            dancer.setJukeboxPos(dancing ? jukeBox : null);
        }
    }




    public void blockRenderingEntity(UUID id) {
    }

    public void releaseRenderingEntity(UUID id) {
    }

    public boolean isFirstPersonPlayer(Entity entity) {
        return false;
    }

    public Player getClientSidePlayer() {
        return null;
    }
}
