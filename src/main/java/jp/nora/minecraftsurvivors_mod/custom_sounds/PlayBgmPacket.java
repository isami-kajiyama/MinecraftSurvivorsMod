package jp.nora.minecraftsurvivors_mod.custom_sounds;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class PlayBgmPacket {
    private final ResourceLocation soundEventLocation;
    private final boolean loop;

    public PlayBgmPacket(ResourceLocation soundEventLocation, boolean loop) {
        this.soundEventLocation = soundEventLocation;
        this.loop = loop;
    }

    public static void encode(PlayBgmPacket packet, PacketBuffer buffer) {
        buffer.writeResourceLocation(packet.soundEventLocation);
        buffer.writeBoolean(packet.loop);
    }

    public static PlayBgmPacket decode(PacketBuffer buffer) {
        ResourceLocation soundEventLocation = buffer.readResourceLocation();
        boolean loop = buffer.readBoolean();
        return new PlayBgmPacket(soundEventLocation, loop);
    }

    public static void handle(PlayBgmPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Implement the logic for playing the BGM on the client-side here
        });
        ctx.get().setPacketHandled(true);
    }
}
