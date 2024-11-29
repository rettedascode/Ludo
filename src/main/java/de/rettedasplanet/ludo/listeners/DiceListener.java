package de.rettedasplanet.ludo.listeners;

import de.rettedasplanet.ludo.LudoPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Random;

public class DiceListener implements Listener {

    private final LudoPlugin plugin;

    public DiceListener(LudoPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDiceClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getGameState().isCurrentPlayer(player)) {
            player.sendMessage(plugin.getMessageManager().get("errors.not_your_turn"));
            return;
        }

        int diceRoll = new Random().nextInt(6) + 1;
        player.sendMessage(plugin.getMessageManager().get("game.dice_roll", Map.of("roll", String.valueOf(diceRoll))));
        plugin.getGameState().rollDice(player, diceRoll);
    }
}
