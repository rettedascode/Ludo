# LudoPlugin

Ein Minecraft-Plugin für das Spiel "Mensch ärgere dich nicht" (Ludo)

<hr>

## Features

- **Spielfeld**: Automatische Generierung eines Spielfelds in einer Void-Welt.
- **Void-Welt**:
    - Kein Daylight-Cycle.
    - Schwierigkeitsgrad: Peaceful.
    - Keines Mobs (freundlich oder feindlich).
- **Mechaniken**:
    - Spielfiguren als farbige Kerzen auf unsichtbaren ArmorStands.
    - Würfelkopf für den aktuellen Spieler, um zu würfeln.
    - Farbige Rüstung für Spieler entsprechend ihrer Farbe.
    - Spielerbewegung basierend auf Würfelergebnissen.
    - Schlagen anderer Figuren, die zurück auf das Startfeld geschickt werden.
    - Zielbereiche für jede Spielfarbe.
- **Nachrichten**:
    - Konfigurierbar über die Datei `message.yml`.

<hr>

## Voraussetzungen

- **Minecraft Server**: Version 1.21 oder höher.
- **Java**: Version 17 oder höher.
- **Spigot API**: Das Plugin verwendet die Spigot-API.

<hr>

## Installation

1. **Plugin kompilieren**:
    - Stelle sicher, dass Maven installiert ist.
    - Navigiere zum Projektverzeichnis und führe den Befehl aus:
      ```bash
      mvn clean install
      ```

2. **Plugin installieren**:
    - Kopiere die generierte `.jar`-Datei aus dem Ordner `target/` in den `plugins/`-Ordner deines Minecraft-Servers.

3. **Server starten**:
    - Starte deinen Server, um das Plugin zu laden.
    - Die Konfigurationsdateien (`plugin.yml`, `message.yml`) werden automatisch generiert.

<hr>

## Konfiguration

### **message.yml**

Hier kannst du alle Nachrichten anpassen, die das Plugin im Spiel verwendet. Beispiele:

```yaml
prefix: "&6[Ludo] "
errors:
  not_enough_players: "Mindestens 2 Spieler sind erforderlich, um das Spiel zu starten."
  not_your_turn: "Es ist nicht dein Zug!"
game:
  start: "Das Spiel beginnt jetzt!"
  dice_roll: "Du hast eine &6{roll} &7gewürfelt!"
```
<hr>