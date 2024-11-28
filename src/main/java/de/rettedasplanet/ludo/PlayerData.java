package de.rettedasplanet.ludo;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private final String name;
    private final int colorId;
    private final List<Integer> pieces;

    public PlayerData(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
        this.pieces = new ArrayList<>();
        // Initialisiere die Figuren auf den Startfeldern
        for (int i = 0; i < 4; i++) {
            pieces.add(-1); // -1 bedeutet: Figur ist auf dem Startfeld
        }
    }

    public String getName() {
        return name;
    }

    public int getColorId() {
        return colorId;
    }

    public List<Integer> getPieces() {
        return pieces;
    }

    public void movePiece(int pieceIndex, int steps) {
        int currentPos = pieces.get(pieceIndex);
        pieces.set(pieceIndex, currentPos + steps);
    }

    public boolean allPiecesInHome() {
        return pieces.stream().allMatch(pos -> pos >= 40); // Zielpositionen ab 40
    }
}
