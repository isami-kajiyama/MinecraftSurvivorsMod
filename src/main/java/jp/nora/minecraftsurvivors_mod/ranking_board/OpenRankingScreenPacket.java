package jp.nora.minecraftsurvivors_mod.ranking_board;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import jp.nora.minecraftsurvivors_mod.MinecraftSurvivors_mod;


import java.util.function.Supplier;

public class OpenRankingScreenPacket {

    public OpenRankingScreenPacket() {}

    public static void encode(OpenRankingScreenPacket packet, PacketBuffer buffer) {}

    public static OpenRankingScreenPacket decode(PacketBuffer buffer) {
        return new OpenRankingScreenPacket();
    }

    public static void handle(OpenRankingScreenPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //MinecraftSurvivors_mod.displayRankingScreen();
        });
        ctx.get().setPacketHandled(true);
    }
}
