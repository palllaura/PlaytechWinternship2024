package main.java.playtech.service;

import main.java.playtech.model.Player;
import main.java.playtech.model.PlayerAction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class OutputWriter {
    private static final String RESULTS_FILE_PATH = "src/main/java/playtech/results.txt";

    public void writeLegitimatePlayers(List<Player> legitimatePlayers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULTS_FILE_PATH))) {
            if (legitimatePlayers.isEmpty()) {
                writer.newLine();
            } else {
                legitimatePlayers.sort(Comparator.comparing(Player::getPlayerId));
                for (Player player : legitimatePlayers) {
                    BigDecimal winRate = calculateWinRate(player);
                    writer.write(String.format("%s %d %.2f\n", player.getPlayerId(), player.getAccountBalance(), winRate));
                }
            }
            writer.newLine();
        }
    }

    public void writeIllegitimatePlayers(List<PlayerAction> illegitimateActions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULTS_FILE_PATH, true))) {
            if (illegitimateActions.isEmpty()) {
                writer.newLine();
            } else {
                illegitimateActions.sort(Comparator.comparing(PlayerAction::getPlayerId));
                for (PlayerAction illegitimateAction : illegitimateActions) {
                    writer.write(String.format("%s %s %s %d %s\n",
                            illegitimateAction.playerId, illegitimateAction.action, illegitimateAction.matchId,
                            illegitimateAction.coins, illegitimateAction.betSide));
                }
            }
            writer.newLine();
        }
    }

    public void writeCasinoBalanceChange(long casinoBalance) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULTS_FILE_PATH, true))) {
            writer.write(String.format("%d", casinoBalance));
        }
    }

    private BigDecimal calculateWinRate(Player player) {
        if (player.getPlacedBets() == 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal gamesWon = new BigDecimal(player.getWinCount());
            BigDecimal placedBets = new BigDecimal(player.getPlacedBets());
            return gamesWon.divide(placedBets, 2, RoundingMode.HALF_UP);
        }
    }
}
