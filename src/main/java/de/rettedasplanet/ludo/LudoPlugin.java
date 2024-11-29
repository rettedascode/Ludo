package de.rettedasplanet.ludo;

import de.rettedasplanet.ludo.commands.LudoCommand;
import de.rettedasplanet.ludo.game.GameBoard;
import de.rettedasplanet.ludo.game.GameBoardGenerator;
import de.rettedasplanet.ludo.game.GameState;
import de.rettedasplanet.ludo.game.PlayerData;
import de.rettedasplanet.ludo.listeners.DiceListener;
import de.rettedasplanet.ludo.utils.MessageManager;
import de.rettedasplanet.ludo.utils.VoidWorldGenerator;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LudoPlugin extends JavaPlugin {

    private MessageManager messageManager;
    private GameBoardGenerator gameBoardGenerator;
    private World gameWorld;
    private final Map<UUID, PlayerData> playersInGame = new HashMap<>();
    private GameState gameState;

    @Override
    public void onEnable() {
        getLogger().info("LudoPlugin aktiviert!");

        messageManager = new MessageManager(getDataFolder());
        gameBoardGenerator = new GameBoardGenerator();

        getCommand("ludo").setExecutor(new LudoCommand(this));
        getServer().getPluginManager().registerEvents(new DiceListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("LudoPlugin deaktiviert!");
        if (gameWorld != null) unloadAndDeleteWorld(gameWorld);
    }

    public World createGameWorld() {
        String worldName = "ludo_void_world_" + System.currentTimeMillis();
        return VoidWorldGenerator.createVoidWorld(worldName);
    }

    public void startGame() {
        if (playersInGame.size() < 2) {
            broadcastMessage("errors.not_enough_players");
            return;
        }

        gameWorld = createGameWorld();
        gameBoardGenerator.generateLudoGameBoardWithAnimation(gameWorld, () -> {
            broadcastMessage("game.start");

            for (UUID playerId : playersInGame.keySet()) {
                var player = getServer().getPlayer(playerId);
                if (player != null) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.teleport(new Location(gameWorld, 0, 64, 0));
                }
            }
            gameState = new GameState(playersInGame, (GameBoard) gameWorld);
            gameState.startTurn();
        });
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public Map<UUID, PlayerData> getPlayersInGame() {
        return playersInGame;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void broadcastMessage(String path) {
        String message = messageManager.get(path);
        getServer().broadcastMessage(message);
    }

    private void unloadAndDeleteWorld(World world) {
        getServer().unloadWorld(world, false);
        var folder = world.getWorldFolder();
        if (folder.exists()) {
            for (var file : folder.listFiles()) file.delete();
            folder.delete();
        }
    }
}
