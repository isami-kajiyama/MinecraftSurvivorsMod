package jp.nora.minecraftsurvivors_mod.custom_sounds;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class StopBgmPacket {
    private final ResourceLocation soundEventLocation;

    public StopBgmPacket(ResourceLocation soundEventLocation) {
        this.soundEventLocation = soundEventLocation;
    }

    public static void encode(StopBgmPacket packet, PacketBuffer buffer) {
        buffer.writeResourceLocation(packet.soundEventLocation);
    }

    public static StopBgmPacket decode(PacketBuffer buffer) {
        ResourceLocation soundEventLocation = buffer.readResourceLocation();
        return new StopBgmPacket(soundEventLocation);
    }

    public static void handle(StopBgmPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Implement the logic for stopping the BGM on the client-side here
        });
        ctx.get().setPacketHandled(true);
    }
}
