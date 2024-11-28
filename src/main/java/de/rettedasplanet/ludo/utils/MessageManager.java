package de.rettedasplanet.ludo.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final FileConfiguration messages;
    private final Map<String, String> cachedMessages = new HashMap<>();
    private final String prefix;

    public MessageManager(File pluginFolder) {
        File messageFile = new File(pluginFolder, "message.yml");
        if (!messageFile.exists()) {
            try {
                if (pluginFolder.mkdirs() && messageFile.createNewFile()) {
                    System.out.println("message.yml wurde erstellt.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.messages = YamlConfiguration.loadConfiguration(messageFile);

        // Standardnachrichten hinzufügen, falls nicht vorhanden
        addDefaultMessages();

        // Prefix aus der Datei laden
        this.prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix", "&6[Ludo] "));
    }

    public String get(String path) {
        return cachedMessages.computeIfAbsent(path, key -> {
            String msg = messages.getString(key, "§cUnbekannte Nachricht: " + key);
            return prefix + ChatColor.translateAlternateColorCodes('&', msg);
        });
    }

    public String get(String path, Map<String, String> placeholders) {
        String message = get(path);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }

    private void addDefaultMessages() {
        addDefault("prefix", "&6[Ludo] ");
        addDefault("errors.not_a_player", "Nur Spieler können diesen Befehl ausführen!");
        addDefault("errors.not_enough_players", "Mindestens 2 Spieler sind erforderlich, um das Spiel zu starten.");
        addDefault("errors.usage", "Benutze: {usage}");
        addDefault("game.joined", "Du bist dem Ludo-Spiel beigetreten!");
        addDefault("game.start", "Das Spiel beginnt jetzt!");

        try {
            messages.save(new File("message.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDefault(String path, String value) {
        if (!messages.contains(path)) {
            messages.set(path, value);
        }
    }
}

