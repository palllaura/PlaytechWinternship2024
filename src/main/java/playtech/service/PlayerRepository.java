package main.java.playtech.service;

import main.java.playtech.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {

    private final HashMap<UUID, Player> players = new HashMap<>();

    public Player getById(UUID playerId) {
        if (players.get(playerId) == null) {
            return addPlayer(playerId);
        }
        return players.get(playerId);
    }

    public List<Player> getLegitimatePlayers() {
        List<Player> legitimatePlayers = new ArrayList<>();

        for (Player player : players.values()) {
            if (!player.isIllegal()) {
                legitimatePlayers.add(player);
            }
        }
        return legitimatePlayers;
    }

    private Player addPlayer(UUID playerId) {
        Player newPlayer = new Player(playerId);
        players.put(playerId, newPlayer);
        return newPlayer;
    }
}
