package jp.nora.minecraftsurvivors_mod.custom_sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("minecraftsurvivors_mod", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, PlayBgmPacket.class, PlayBgmPacket::encode, PlayBgmPacket::decode, PlayBgmPacket::handle);
        INSTANCE.registerMessage(1, StopBgmPacket.class, StopBgmPacket::encode, StopBgmPacket::decode, StopBgmPacket::handle);
    }
}
