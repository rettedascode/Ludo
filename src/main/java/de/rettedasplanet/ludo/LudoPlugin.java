
package de.rettedasplanet.ludo;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LudoPlugin extends JavaPlugin {

    private GameBoardGenerator gameBoardGenerator; // Spielfeld-Generator
    private World gameWorld; // Die aktuell genutzte Spielwelt
    private final List<String> playersInLobby = new ArrayList<>(); // Spieler in der Lobby

    @Override
    public void onEnable() {
        getLogger().info("LudoPlugin aktiviert!");

        // Initialisiere den Spielfeld-Generator
        gameBoardGenerator = new GameBoardGenerator();

        // Registriere den Hauptbefehl /ludo
        getCommand("ludo").setExecutor(new LudoCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("LudoPlugin deaktiviert!");

        // Welt entladen und löschen, falls vorhanden
        if (gameWorld != null) {
            unloadAndDeleteWorld(gameWorld);
        }
    }

    public void addPlayerToLobby(String playerName) {
        if (!playersInLobby.contains(playerName)) {
            playersInLobby.add(playerName);
        }
    }

    public void removePlayerFromLobby(String playerName) {
        playersInLobby.remove(playerName);
    }

    public boolean isGameReadyToStart() {
        return playersInLobby.size() >= 2; // Mindestens 2 Spieler erforderlich
    }

    public List<String> getPlayersInLobby() {
        return playersInLobby;
    }

    public void startGame() {
        // Erstelle die Welt
        gameWorld = createGameWorld();

        // Generiere das Spielfeld mit einer Animation
        gameBoardGenerator.generateLudoGameBoardWithAnimation(gameWorld, () -> {
            Bukkit.broadcastMessage("Das Spielfeld wurde generiert! Das Spiel beginnt!");
        });
    }

    public World createGameWorld() {
        String worldName = "ludo_world_" + System.currentTimeMillis();
        return getServer().createWorld(new org.bukkit.WorldCreator(worldName));
    }

    public void unloadAndDeleteWorld(World world) {
        if (world != null) {
            getLogger().info("Entlade und lösche die Welt: " + world.getName());
            getServer().unloadWorld(world, false);

            java.io.File worldFolder = world.getWorldFolder();
            deleteWorld(worldFolder);
        }
    }

    private void deleteWorld(java.io.File path) {
        if (path.exists()) {
            java.io.File[] files = path.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    deleteWorld(file);
                }
            }
            path.delete();
        }
    }
}
