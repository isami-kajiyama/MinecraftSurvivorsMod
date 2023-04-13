package jp.nora.minecraftsurvivors_mod.ranking_board;

import com.mojang.blaze3d.matrix.MatrixStack;
import jp.nora.minecraftsurvivors_mod.MinecraftSurvivors_mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class NameInputScreen extends Screen {
    private TextFieldWidget nameInput;
    private final StringTextComponent title;

    public NameInputScreen() {
        super(new StringTextComponent("Enter your name"));
        title = new StringTextComponent("Enter your name");
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        nameInput = new TextFieldWidget(this.font, centerX - 100, centerY - 20, 200, 20, new StringTextComponent(""));
        this.addButton(nameInput);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 40, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        nameInput.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter key or numpad Enter key
            String playerName = nameInput.getText();
            if (!playerName.isEmpty()) {
                // プレイヤーをリスポーンさせる
                Minecraft.getInstance().player.respawnPlayer();

                ScoreManager.addScore(playerName, MinecraftSurvivors_mod.getScore());
                Minecraft.getInstance().displayGuiScreen(new RankingScreen());
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}

