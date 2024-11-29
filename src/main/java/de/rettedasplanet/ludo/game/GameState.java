package de.rettedasplanet.ludo.game;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class GameState {

    private final Map<UUID, PlayerData> players;
    private final World world;
    private Iterator<Map.Entry<UUID, PlayerData>> turnIterator;
    private Map.Entry<UUID, PlayerData> currentPlayer;

    public GameState(Map<UUID, PlayerData> players, World world) {
        this.players = players;
        this.world = world;
        this.turnIterator = players.entrySet().iterator();
    }

    public void startTurn() {
        if (!turnIterator.hasNext()) turnIterator = players.entrySet().iterator();
        currentPlayer = turnIterator.next();
        Player player = Bukkit.getPlayer(currentPlayer.getKey());
        if (player != null) player.sendMessage("Du bist am Zug!");
    }

    public boolean isCurrentPlayer(Player player) {
        return currentPlayer != null && currentPlayer.getKey().equals(player.getUniqueId());
    }

    public void rollDice(Player player, int diceRoll) {
        // Spiellogik für Würfelwurf hier hinzufügen
    }
}
