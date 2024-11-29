package de.rettedasplanet.ludo.commands;

import de.rettedasplanet.ludo.LudoPlugin;
import de.rettedasplanet.ludo.game.PlayerData;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LudoCommand implements CommandExecutor {

    private final LudoPlugin plugin;
    private final Material[] candleMaterials = {
            Material.RED_CANDLE,
            Material.YELLOW_CANDLE,
            Material.GREEN_CANDLE,
            Material.BLUE_CANDLE
    };
    private final Color[] playerColors = {
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE
    };

    public LudoCommand(LudoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessageManager().get("errors.not_a_player"));
            return true;
        }

        int currentPlayerCount = plugin.getPlayersInGame().size();
        if (currentPlayerCount >= candleMaterials.length) {
            player.sendMessage(plugin.getMessageManager().get("errors.too_many_players"));
            return true;
        }

        // Erstelle PlayerData mit den entsprechenden Materialien und Farben
        PlayerData playerData = new PlayerData(
                player.getName(),
                currentPlayerCount,
                candleMaterials[currentPlayerCount],
                playerColors[currentPlayerCount]
        );

        // Füge den Spieler in die Map ein
        plugin.getPlayersInGame().put(player.getUniqueId(), playerData);
        player.sendMessage(plugin.getMessageManager().get("game.joined"));

        // Überprüfe, ob genügend Spieler vorhanden sind, um das Spiel zu starten
        if (plugin.getPlayersInGame().size() >= 2) {
            plugin.startGame();
        } else {
            player.sendMessage(plugin.getMessageManager().get("errors.not_enough_players"));
        }
        return true;
    }
}
