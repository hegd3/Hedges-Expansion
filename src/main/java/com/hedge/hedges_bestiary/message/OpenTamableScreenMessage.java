package com.hedge.hedges_bestiary.message;

import com.hedge.hedges_bestiary.events.ClientEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenTamableScreenMessage {
    private final int id;
    private final int syncId;

    public OpenTamableScreenMessage(int id, int syncId) {
        this.id = id;
        this.syncId = syncId;
    }

    public static OpenTamableScreenMessage read(FriendlyByteBuf buf) {
        int id = buf.readInt();
        int syncId = buf.readInt();
        return new OpenTamableScreenMessage(id, syncId);
    }

    public static void write(OpenTamableScreenMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.id);
        buf.writeInt(packet.syncId);
    }

    public static void handle(OpenTamableScreenMessage packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientEvent.openTamableScreen(packet));
        ctx.get().setPacketHandled(true);
    }

    public int getId() {
        return this.id;
    }


    public int getSyncId() {
        return this.syncId;
    }
}
