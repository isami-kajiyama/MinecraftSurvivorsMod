package jp.nora.minecraftsurvivors_mod.countdown;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "minecraftsurvivors_mod")
public class CountdownOverlay extends AbstractGui {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static int countdownTime = 0;

    @SubscribeEvent
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            renderCountdown(event.getMatrixStack());
        }
    }

    public static void renderCountdown(MatrixStack matrixStack) {
        // フォントサイズを大きく
        float fontSize = 10.0F;

        if (countdownTime > 0) {
            int scaledWidth = minecraft.getMainWindow().getScaledWidth();
            int scaledHeight = minecraft.getMainWindow().getScaledHeight();
            String countdownText = String.valueOf(countdownTime);
            int textWidth = minecraft.fontRenderer.getStringWidth(countdownText);
            int x = scaledWidth / 2;
            int y = scaledHeight / 2;

            matrixStack.push();
            matrixStack.scale(fontSize, fontSize, fontSize);

            // 文字を70%透明に設定
            minecraft.fontRenderer.drawString(matrixStack, countdownText, x / fontSize, y / fontSize, 0x80FFFFFF);
            matrixStack.pop();
        }
    }

    public static void setCountdownTime(int countdownTime) {
        CountdownOverlay.countdownTime = countdownTime;
    }
}
