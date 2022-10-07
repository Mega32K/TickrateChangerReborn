package net.mega.tc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;
    private final float percent;

    public Networking(FriendlyByteBuf friendlyByteBuf) {
        percent = friendlyByteBuf.readFloat();
    }

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Main.MODID, "first_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(Networking.class, nextID())
                .encoder(Networking::toBytes)
                .decoder(Networking::new)
                .consumer(Networking::handler)
                .add();
    }

    public Networking(float p) {
        this.percent = p;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(this.percent);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                Main.changeAll(percent);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}