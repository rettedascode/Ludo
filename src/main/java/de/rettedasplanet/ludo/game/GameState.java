package de.rettedasplanet.ludo.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GameState {

    private final Map<UUID, PlayerData> players;
    private final GameBoard gameBoard;
    private Iterator<Map.Entry<UUID, PlayerData>> turnIterator;
    private Map.Entry<UUID, PlayerData> currentPlayer;

    public GameState(Map<UUID, PlayerData> players, GameBoard gameBoard) {
        this.players = players;
        this.gameBoard = gameBoard;
        this.turnIterator = players.entrySet().iterator();
    }

    public void startTurn() {
        if (!turnIterator.hasNext()) {
            turnIterator = players.entrySet().iterator();
        }
        currentPlayer = turnIterator.next();
        Player player = Bukkit.getPlayer(currentPlayer.getKey());
        if (player != null) {
            player.sendMessage("Du bist am Zug! Würfle mit deinem Würfelkopf.");
        }
    }

    public boolean isCurrentPlayer(Player player) {
        return currentPlayer != null && currentPlayer.getKey().equals(player.getUniqueId());
    }

    public void rollDice(Player player, int diceRoll) {
        PlayerData playerData = currentPlayer.getValue();

        // Beispiel: Bewege die erste Figur, die sich bewegen kann
        for (int i = 0; i < playerData.getPieces().size(); i++) {
            if (canMove(playerData, i, diceRoll)) {
                playerData.movePiece(i, diceRoll, gameBoard);
                player.sendMessage("Figur " + (i + 1) + " wurde um " + diceRoll + " Felder bewegt.");
                checkCollisions(playerData, i);
                startTurn(); // Nächster Spieler
                return;
            }
        }

        player.sendMessage("Keine Figur kann bewegt werden. Dein Zug endet.");
        startTurn();
    }

    private boolean canMove(PlayerData playerData, int pieceIndex, int diceRoll) {
        int currentPos = playerData.getPositions().get(pieceIndex);
        return currentPos == -1 && diceRoll == 6 || currentPos >= 0 && currentPos + diceRoll < gameBoard.getBoardSize();
    }

    private void checkCollisions(PlayerData playerData, int pieceIndex) {
        Location pieceLocation = playerData.getPieces().get(pieceIndex).getLocation();

        for (PlayerData otherPlayer : players.values()) {
            if (otherPlayer == playerData) continue;

            for (int i = 0; i < otherPlayer.getPieces().size(); i++) {
                ArmorStand otherPiece = otherPlayer.getPieces().get(i);
                if (otherPiece.getLocation().equals(pieceLocation)) {
                    // Andere Figur zurück auf die Startposition schicken
                    otherPiece.teleport(Objects.requireNonNull(pieceLocation.getWorld()).getSpawnLocation());
                    otherPlayer.getPositions().set(i, -1);
                    Bukkit.broadcastMessage("Figur von " + otherPlayer.getName() + " wurde geschlagen!");
                }
            }
        }
    }
}
