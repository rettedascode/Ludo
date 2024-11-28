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

        // Überprüfe, ob der Spieler am Zug ist
        if (!plugin.getGameState().isCurrentPlayer(player)) {
            player.sendMessage(plugin.getMessageManager().get("errors.not_your_turn"));
            return;
        }

        // Würfelanimation und Ergebnis anzeigen
        showDiceAnimation(player, () -> {
            int diceRoll = new Random().nextInt(6) + 1;
            player.sendMessage(plugin.getMessageManager().get("game.dice_roll", Map.of("roll", String.valueOf(diceRoll))));

            // Übergib das Ergebnis an die Spiellogik
            plugin.getGameState().rollDice(player, diceRoll);
        });
    }

    private void showDiceAnimation(Player player, Runnable onComplete) {
        // Animationen für Würfelzahlen
        String[] animations = {"⚀", "⚁", "⚂", "⚃", "⚄", "⚅"};
        Random random = new Random();

        // Animation abspielen
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            int frame = 0;

            @Override
            public void run() {
                if (frame >= 10) { // 10 Frames Animation
                    plugin.getServer().getScheduler().cancelTasks(plugin);
                    onComplete.run(); // Animation beendet
                    return;
                }

                // Zeige eine zufällige Würfelzahl während der Animation
                player.sendTitle("", "§6" + animations[random.nextInt(6)], 0, 10, 0);
                frame++;
            }
        }, 0, 2); // Animation alle 2 Ticks aktualisieren
    }
}
