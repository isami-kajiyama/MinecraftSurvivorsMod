package jp.nora.minecraftsurvivors_mod.ranking_board;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class RankingScreen extends Screen {
    private ArrayList<PlayerScore> scores;

    public RankingScreen() {
        super(new StringTextComponent("Ranking"));
        scores = ScoreManager.loadScores();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        for (int i = 0; i < scores.size() && i < 10; i++) {
            PlayerScore score = scores.get(i);
            String scoreText = String.format("%s - %d", score.getPlayerName(), score.getScore());
            int y = centerY - 60 + i * 12;
            this.font.drawString(matrixStack, scoreText, centerX - this.font.getStringWidth(scoreText) / 2, y, 0xFFFFFF);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            Minecraft.getInstance().displayGuiScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}

