package de.rettedasplanet.ludo.commands;

import de.rettedasplanet.ludo.LudoPlugin;
import de.rettedasplanet.ludo.PlayerData;
import de.rettedasplanet.ludo.game.GameBoardGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class LudoCommand implements CommandExecutor {

    private final LudoPlugin plugin;

    public LudoCommand(LudoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageManager().get("errors.not_a_player"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || !args[0].equalsIgnoreCase("join")) {
            player.sendMessage(plugin.getMessageManager().get("errors.usage", Map.of("usage", "/ludo join")));
            return true;
        }

        plugin.getPlayersInGame().put(player.getUniqueId(), new PlayerData(player.getName(), plugin.getPlayersInGame().size(), null));
        player.sendMessage(plugin.getMessageManager().get("game.joined"));
        if (plugin.getPlayersInGame().size() >= 2) {
            plugin.startGame(new GameBoardGenerator());
        } else {
            player.sendMessage(plugin.getMessageManager().get("errors.not_enough_players"));
        }

        return true;
    }
}
