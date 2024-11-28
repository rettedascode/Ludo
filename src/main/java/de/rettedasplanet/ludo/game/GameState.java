package de.rettedasplanet.ludo.game;

import de.rettedasplanet.ludo.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class GameState {

    private final Map<UUID, PlayerData> players;
    private Iterator<Map.Entry<UUID, PlayerData>> turnIterator;
    private final World world;

    private Map.Entry<UUID, PlayerData> currentPlayer;

    public GameState(Map<UUID, PlayerData> players, World world) {
        this.players = players;
        this.world = world;
        this.turnIterator = players.entrySet().iterator();
    }

    public void startTurn() {
        if (!turnIterator.hasNext()) {
            resetTurnIterator();
        }

        currentPlayer = turnIterator.next();
        PlayerData playerData = currentPlayer.getValue();
        Player player = Bukkit.getPlayer(currentPlayer.getKey());

        if (player == null) {
            Bukkit.broadcastMessage(playerData.getName() + " ist nicht online. Überspringe den Zug.");
            startTurn();
            return;
        }

        Bukkit.broadcastMessage(playerData.getName() + " ist am Zug. Bitte würfle mit dem Würfelkopf!");
    }

    private void resetTurnIterator() {
        turnIterator = players.entrySet().iterator();
    }
}