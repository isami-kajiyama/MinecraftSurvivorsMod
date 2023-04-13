package jp.nora.minecraftsurvivors_mod.regi;

import jp.nora.minecraftsurvivors_mod.entity.render.RedZombieEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "minecraftsurvivors_mod", bus = Bus.MOD, value = Dist.CLIENT)
public class CommonEventBusSubscriber {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        mobAttributes();
    }

    private static void mobAttributes(){
        DeferredWorkQueue.runLater(()->{
            GlobalEntityTypeAttributes.put(MobEntityTypes.RED_ZOMBIE.get(), RedZombieEntity.registerAttributes().create());
        });
    }


}
