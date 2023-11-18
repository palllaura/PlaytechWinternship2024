package main.java.playtech.model;

import java.util.UUID;

public class Player {
    private final UUID playerId;
    private long accountBalance;
    private boolean isIllegal;
    private int placedBets;
    private int winCount;
    private int amountWon;

    public Player(UUID playerId) {
        this.playerId = playerId;
        this.accountBalance = 0;
        this.isIllegal = false;
        this.placedBets = 0;
        this.winCount = 0;
        this.amountWon = 0;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getAccountBalance() {
        return accountBalance;
    }

    public boolean checkBalance(int coins) {
        return coins <= accountBalance;
    }

    public void addFunds(int coins) {
        this.accountBalance += coins;
    }

    public void removeFunds(int coins) {
        this.accountBalance -= coins;
    }

    public void updateWinLossAmount(int coins) {
        this.amountWon += coins;
    }

    public boolean isIllegal() {
        return isIllegal;
    }

    public void setIllegal(boolean illegal) {
        isIllegal = illegal;
    }

    public void increasePlacedBets() {
        this.placedBets++;
    }

    public void increaseWinCount() {
        this.winCount++;
    }

    public int getPlacedBets() {
        return placedBets;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getAmountWon() {
        return amountWon;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", accountBalance=" + accountBalance +
                ", isIllegal=" + isIllegal +
                ", placedBets=" + placedBets +
                ", gamesWon=" + winCount +
                '}';
    }
}
