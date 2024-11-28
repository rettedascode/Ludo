package de.rettedasplanet.ludo;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GameState {

    private final Map<UUID, PlayerData> players;
    private Iterator<Map.Entry<UUID, PlayerData>> turnIterator;

    private Map.Entry<UUID, PlayerData> currentPlayer;

    public GameState(Map<UUID, PlayerData> players, World world) {
        this.players = players;
        this.turnIterator = players.entrySet().iterator();
        this.currentPlayer = null;
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

        Bukkit.broadcastMessage(playerData.getName() + " ist am Zug. Bitte würfle mit /roll.");
    }

    public void rollDice(Player player) {
        if (!currentPlayer.getKey().equals(player.getUniqueId())) {
            player.sendMessage("Du bist nicht am Zug!");
            return;
        }

        int diceRoll = new Random().nextInt(6) + 1;
        player.sendMessage("Du hast eine " + diceRoll + " gewürfelt!");

        // Logik zum Bewegen der Figuren implementieren
        handlePieceMovement(player, diceRoll);
    }

    private void handlePieceMovement(Player player, int diceRoll) {
        PlayerData playerData = currentPlayer.getValue();

        // Beispiel: Wähle die erste Figur, die sich bewegen kann
        for (int i = 0; i < playerData.getPieces().size(); i++) {
            int currentPos = playerData.getPieces().get(i);
            if (currentPos == -1 && diceRoll == 6) {
                // Figur ins Spiel bringen
                playerData.movePiece(i, 0);
                player.sendMessage("Figur " + (i + 1) + " wurde ins Spiel gebracht.");
                startTurn();
                return;
            } else if (currentPos != -1) {
                // Figur bewegen
                playerData.movePiece(i, diceRoll);
                player.sendMessage("Figur " + (i + 1) + " wurde um " + diceRoll + " Felder bewegt.");
                startTurn();
                return;
            }
        }

        player.sendMessage("Du kannst keine Figuren bewegen. Dein Zug endet.");
        startTurn();
    }

    private void resetTurnIterator() {
        turnIterator = players.entrySet().iterator();
    }
}