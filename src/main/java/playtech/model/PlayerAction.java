package main.java.playtech.model;

import java.util.UUID;

public class PlayerAction {
    public UUID playerId;
    public Action action;
    public UUID matchId;
    public int coins;
    public BetSide betSide;

    public PlayerAction(UUID playerId, Action action, UUID matchId, int coins, BetSide betSide) {
        this.playerId = playerId;
        this.action = action;
        this.matchId = matchId;
        this.coins = coins;
        this.betSide = betSide;
    }

    public PlayerAction() {}

    public UUID getPlayerId() {
        return playerId;
    }

    public Action getAction() {
        return action;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public int getAmount() {
        return coins;
    }

    public BetSide getBetSide() {
        return betSide;
    }

    public enum Action {
        DEPOSIT,
        BET,
        WITHDRAW
    }

    public enum BetSide {
        A,
        B
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "playerId=" + playerId +
                ", action=" + action +
                ", matchId=" + matchId +
                ", coins=" + coins +
                ", betSide=" + betSide +
                '}';
    }
}
