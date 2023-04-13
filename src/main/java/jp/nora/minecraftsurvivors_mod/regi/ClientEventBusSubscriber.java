package jp.nora.minecraftsurvivors_mod.regi;

import jp.nora.minecraftsurvivors_mod.entity.render.RedZombieRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "minecraftsurvivors_mod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        renderRegister(event);
    }

    public static void renderRegister(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(MobEntityTypes.RED_ZOMBIE.get(), RedZombieRenderer::new);
    }
}
