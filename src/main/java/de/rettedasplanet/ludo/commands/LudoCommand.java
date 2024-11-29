package de.rettedasplanet.ludo.commands;

import de.rettedasplanet.ludo.LudoPlugin;
import de.rettedasplanet.ludo.game.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LudoCommand implements CommandExecutor {

    private final LudoPlugin plugin;

    public LudoCommand(LudoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessageManager().get("errors.not_a_player"));
            return true;
        }

        plugin.getPlayersInGame().put(player.getUniqueId(), new PlayerData(player.getName()));
        player.sendMessage(plugin.getMessageManager().get("game.joined"));
        if (plugin.getPlayersInGame().size() >= 2) {
            plugin.startGame();
        } else {
            player.sendMessage(plugin.getMessageManager().get("errors.not_enough_players"));
        }
        return true;
    }
}
