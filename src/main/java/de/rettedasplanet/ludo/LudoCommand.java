package de.rettedasplanet.ludo;

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
            sender.sendMessage("Dieser Befehl kann nur von Spielern verwendet werden.");
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("join")) {
            player.sendMessage("Bitte benutze /ludo join.");
            return true;
        }

        plugin.addPlayerToLobby(player.getName());
        player.sendMessage("Du bist der Ludo-Lobby beigetreten!");

        if (plugin.isGameReadyToStart()) {
            plugin.startGame();
        } else {
            player.sendMessage("Warte auf mindestens 2 Spieler, um das Spiel zu starten...");
        }

        return true;
    }
}
