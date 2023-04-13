//package jp.nora.minecraftsurvivors_mod.events;
//
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.passive.ChickenEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.util.Random;
//
//@Mod.EventBusSubscriber
//public class ChickenSpawner {
//
//    private static final int SPAWN_INTERVAL = 20 * 20; // 20 seconds * 20 ticks per second
//    private static int countdown = SPAWN_INTERVAL;
//
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote) {
//            countdown--;
//
//            if (countdown <= 0) {
//                countdown = SPAWN_INTERVAL;
//
//                World world = event.player.world;
//                Random random = new Random();
//                double spawnX = event.player.getPosX() + random.nextInt(10) - 5;
//                double spawnY = event.player.getPosY();
//                double spawnZ = event.player.getPosZ() + random.nextInt(10) - 5;
//
//                BlockPos spawnPos = new BlockPos(spawnX, spawnY, spawnZ);
//
//                ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
//                chicken.setPosition(spawnX, spawnY, spawnZ);
//
//                world.addEntity(chicken);
//            }
//        }
//    }
//}
