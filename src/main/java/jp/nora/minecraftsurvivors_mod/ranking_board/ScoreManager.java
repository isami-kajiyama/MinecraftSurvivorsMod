package jp.nora.minecraftsurvivors_mod.ranking_board;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.*;

public class ScoreManager {
    private static final String SCORE_FILE_PATH = "scores.txt";

    public static void addScore(String playerName, int score) {
        List<PlayerScore> scores = loadScores();
        scores.add(new PlayerScore(playerName, score));
        scores.sort(Comparator.comparing(PlayerScore::getScore).reversed());
        saveScores(scores);
    }

    public static ArrayList<PlayerScore> loadScores() {
        ArrayList<PlayerScore> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    scores.add(new PlayerScore(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }

    private static void saveScores(List<PlayerScore> scores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORE_FILE_PATH))) {
            for (PlayerScore score : scores) {
                writer.println(score.getPlayerName() + "," + score.getScore());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
