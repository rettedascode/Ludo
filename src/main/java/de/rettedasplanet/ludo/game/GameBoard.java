package de.rettedasplanet.ludo.game;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    private final List<Location> boardPositions;

    public GameBoard(World world, int centerX, int centerZ, int baseY) {
        this.boardPositions = new ArrayList<>();

        // Spielfeld-Positionen erstellen (Kreisbahn)
        for (int i = 0; i < 40; i++) {
            double angle = 2 * Math.PI * i / 40; // Winkel für 40 Positionen
            int x = centerX + (int) (Math.cos(angle) * 10);
            int z = centerZ + (int) (Math.sin(angle) * 10);
            boardPositions.add(new Location(world, x, baseY, z));
        }
    }

    public Location getBoardPosition(int index) {
        return boardPositions.get(index % boardPositions.size()); // Zyklische Position
    }

    public int getBoardSize() {
        return boardPositions.size();
    }
}
