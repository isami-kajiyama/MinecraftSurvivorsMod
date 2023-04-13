package jp.nora.minecraftsurvivors_mod.ranking_board;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("minecraftsurvivors_mod", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        INSTANCE.registerMessage(0, OpenRankingScreenPacket.class, OpenRankingScreenPacket::encode, OpenRankingScreenPacket::decode, OpenRankingScreenPacket::handle);
    }
}
