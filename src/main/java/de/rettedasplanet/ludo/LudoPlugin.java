package de.rettedasplanet.ludo;

import de.rettedasplanet.ludo.commands.LudoCommand;
import de.rettedasplanet.ludo.game.GameBoardGenerator;
import de.rettedasplanet.ludo.game.GameState;
import de.rettedasplanet.ludo.listeners.DiceListener;
import de.rettedasplanet.ludo.utils.MessageManager;
import de.rettedasplanet.ludo.utils.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LudoPlugin extends JavaPlugin {

    private MessageManager messageManager;
    private World gameWorld;
    private final Map<UUID, PlayerData> playersInGame = new HashMap<>();
    private GameState gameState;

    @Override
    public void onEnable() {
        getLogger().info("LudoPlugin aktiviert!");

        // Nachrichten-Manager initialisieren
        messageManager = new MessageManager(getDataFolder());

        // Befehle und Listener registrieren
        getCommand("ludo").setExecutor(new LudoCommand(this));
        getServer().getPluginManager().registerEvents(new DiceListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("LudoPlugin deaktiviert!");

        // Welt entladen und löschen, falls vorhanden
        if (gameWorld != null) {
            unloadAndDeleteWorld(gameWorld);
        }
    }

    public World createGameWorld() {
        String worldName = "ludo_void_world_" + System.currentTimeMillis();
        return VoidWorldGenerator.createVoidWorld(worldName);
    }

    public void startGame(GameBoardGenerator gameBoardGenerator) {
        if (playersInGame.size() < 2) {
            Bukkit.broadcastMessage(messageManager.get("errors.not_enough_players"));
            return;
        }

        // Erstelle die Void-Welt und das Spielfeld
        gameWorld = createGameWorld();
        gameBoardGenerator.generateLudoGameBoardWithAnimation(gameWorld, () -> {
            Bukkit.broadcastMessage(messageManager.get("game.start"));

            // Setze alle Spieler in Adventure-Modus und teleportiere sie zum Spielfeld
            for (UUID playerId : playersInGame.keySet()) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.teleport(new Location(gameWorld, 0, 64, 0)); // Spielfeld-Zentrum
                }
            }

            gameState = new GameState(playersInGame, gameWorld);
            gameState.startTurn();
        });
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public Map<UUID, PlayerData> getPlayersInGame() {
        return playersInGame;
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
