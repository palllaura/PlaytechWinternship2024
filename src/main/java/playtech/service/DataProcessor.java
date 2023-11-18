package main.java.playtech.service;

import main.java.playtech.model.Match;
import main.java.playtech.model.Player;
import main.java.playtech.model.PlayerAction;

import java.io.IOException;
import java.util.*;

public class DataProcessor {
    private final DataReader dataReader;
    private final PlayerRepository playerRepository;
    private final OutputWriter outputWriter;
    private final Map<UUID, Match> matches;
    private final List<PlayerAction> illegitimateActions;

    public DataProcessor() {
        this.dataReader = new DataReader();
        this.playerRepository = new PlayerRepository();
        this.outputWriter = new OutputWriter();
        this.matches = new HashMap<>();
        this.illegitimateActions = new ArrayList<>();
    }

    public void processDataAndGenerateResult() {
        processMatchData();
        processPlayerData();
        generateResultsFile();
    }

    private void processMatchData() {
        List<String[]> rawData = dataReader.readMatchData();

        for (String[] row : rawData) {
            Match match = parseRawMatch(row);
            matches.put(match.matchId(), match);
        }
    }

    private void processPlayerData() {
        List<String[]> rawData = dataReader.readPlayerData();

        for (String[] row : rawData) {
            PlayerAction playerAction = parseRawPlayerAction(row);
            processPlayerAction(playerAction);
        }
    }

    private void processPlayerAction(PlayerAction playerAction) {
        Player currentPlayer = playerRepository.getById(playerAction.getPlayerId());

        if (currentPlayer.isIllegal()) {
            return;
        }

        switch (playerAction.getAction()) {
            case DEPOSIT:
                processDeposit(currentPlayer, playerAction.getAmount());
                break;
            case WITHDRAW:
                processWithdrawal(currentPlayer, playerAction.getAmount(), playerAction);
                break;
            case BET:
                processBet(currentPlayer, playerAction.getAmount(), playerAction);
                break;
            default:
                throw new RuntimeException("Unsupported action");
        }
    }

    private void processDeposit(Player currentPlayer, int amount) {
        currentPlayer.addFunds(amount);
    }

    private void processWithdrawal(Player currentPlayer, int amount, PlayerAction playerAction) {
        if (currentPlayer.checkBalance(amount)) {
            currentPlayer.removeFunds(amount);
        } else {
            currentPlayer.setIllegal(true);
            logIllegitimateAction(currentPlayer, playerAction, null);
        }
    }

    private void processBet(Player currentPlayer, int amount, PlayerAction playerAction) {
        Match currentMatch = getMatchById(playerAction.getMatchId());

        if (!currentPlayer.checkBalance(amount)) {
            currentPlayer.setIllegal(true);
            logIllegitimateAction(currentPlayer, playerAction, currentMatch);
            return;
        }

        if (currentMatch == null) {
            return;
        }

        currentPlayer.increasePlacedBets();

        if (Objects.equals(playerAction.getBetSide().toString(), currentMatch.result().toString())) {
            currentPlayer.increaseWinCount();
            int amountWon;
            if (playerAction.getBetSide().toString().equals("A")) {
                amountWon = (int) (playerAction.getAmount() * currentMatch.returnRateA());
            } else {
                amountWon = (int) (playerAction.getAmount() * currentMatch.returnRateB());
            }
            currentPlayer.addFunds(amountWon);
            currentPlayer.updateWinLossAmount(amountWon);
        } else if (currentMatch.result() != Match.Result.DRAW) {
            final int amountLost = playerAction.getAmount();

            currentPlayer.removeFunds(amountLost);
            currentPlayer.updateWinLossAmount(-amountLost);
        }
    }

    private PlayerAction parseRawPlayerAction(String[] row) {
        UUID playerId = UUID.fromString(row[0]);
        PlayerAction.Action action = PlayerAction.Action.valueOf(row[1]);
        UUID matchId = row[2].isEmpty() ? null : UUID.fromString(row[2]);
        int coins = Integer.parseInt(row[3]);
        PlayerAction.BetSide betSide = row.length > 4 && !row[4].isEmpty() ? PlayerAction.BetSide.valueOf(row[4]) : null;

        return new PlayerAction(playerId, action, matchId, coins, betSide);
    }

    private Match parseRawMatch(String[] row) {
        UUID matchId = UUID.fromString(row[0]);
        double returnRateA = Double.parseDouble(row[1]);
        double returnRateB = Double.parseDouble(row[2]);
        Match.Result result = Match.Result.valueOf(row[3]);

        return new Match(matchId, returnRateA, returnRateB, result);
    }

    private Match getMatchById(UUID matchId) {
        return matches.get(matchId);
    }

    private void generateResultsFile() {
        try {
            List<Player> legitimatePlayers = playerRepository.getLegitimatePlayers();
            outputWriter.writeLegitimatePlayers(legitimatePlayers);

            outputWriter.writeIllegitimatePlayers(illegitimateActions);

            outputWriter.writeCasinoBalanceChange(calculateCasinoBalance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void logIllegitimateAction(Player player, PlayerAction playerAction, Match currentMatch) {
        PlayerAction illegitimateAction = new PlayerAction();

        illegitimateAction.playerId = player.getPlayerId();
        illegitimateAction.action = playerAction.getAction();
        illegitimateAction.matchId = currentMatch != null ? currentMatch.matchId() : null;
        illegitimateAction.coins = playerAction.getAmount();
        illegitimateAction.betSide = playerAction.getBetSide();

        illegitimateActions.add(illegitimateAction);
    }

    private long calculateCasinoBalance() {
        long casinoBalance = 0;
        List<Player> legitimatePlayers = playerRepository.getLegitimatePlayers();
        for (Player legitimatePlayer : legitimatePlayers) {
            casinoBalance -= legitimatePlayer.getAmountWon();
        }
        return casinoBalance;
    }
}
