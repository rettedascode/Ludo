package de.rettedasplanet.ludo.game;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerData {

    private final String name;
    private final Material candleMaterial;
    private final List<ArmorStand> pieces;
    private final List<Integer> positions;
    private final Color playerColor;

    public PlayerData(String name, int colorId, Material candleMaterial, Color playerColor) {
        this.name = name;
        this.candleMaterial = candleMaterial;
        this.playerColor = playerColor;
        this.pieces = new ArrayList<>();
        this.positions = new ArrayList<>();
    }

    public void spawnPieces(World world, Location startLocation) {
        for (int i = 0; i < 4; i++) {
            ArmorStand piece = (ArmorStand) world.spawnEntity(
                    startLocation.clone().add(i, 0, 0), EntityType.ARMOR_STAND
            );
            piece.setInvisible(true);
            piece.setMarker(true);
            piece.setCustomNameVisible(true);
            piece.setCustomName(name + " Figur " + (i + 1));
            Objects.requireNonNull(piece.getEquipment()).setHelmet(new ItemStack(candleMaterial));

            pieces.add(piece);
            positions.add(-1); // Startposition
        }
    }

    public List<ArmorStand> getPieces() {
        return pieces;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public String getName() {
        return name;
    }

    public void movePiece(int pieceIndex, int steps, GameBoard gameBoard) {
        int currentPos = positions.get(pieceIndex);
        ArmorStand piece = pieces.get(pieceIndex);

        if (currentPos == -1 && steps == 6) {
            // Figur ins Spiel bringen
            positions.set(pieceIndex, 0);
            smoothMove(piece, piece.getLocation(), gameBoard.getBoardPosition(0));
        } else if (currentPos >= 0 && currentPos + steps < gameBoard.getBoardSize()) {
            // Figur bewegen
            int newPos = currentPos + steps;
            positions.set(pieceIndex, newPos);
            smoothMove(piece, gameBoard.getBoardPosition(currentPos), gameBoard.getBoardPosition(newPos));
        }
    }

    private void smoothMove(ArmorStand piece, Location start, Location end) {
        World world = start.getWorld();
        double distance = start.distance(end);
        int steps = (int) (distance * 20); // Anzahl der Schritte für eine flüssige Bewegung
        double dx = (end.getX() - start.getX()) / steps;
        double dy = (end.getY() - start.getY()) / steps;
        double dz = (end.getZ() - start.getZ()) / steps;

        new BukkitRunnable() {
            int currentStep = 0;

            @Override
            public void run() {
                if (currentStep > steps) {
                    piece.teleport(end); // Endposition
                    cancel();
                    return;
                }

                // Bewegung berechnen
                Location newLocation = start.clone().add(dx * currentStep, dy * currentStep, dz * currentStep);
                piece.teleport(newLocation);

                // Farbige Partikel erzeugen
                assert world != null;
                spawnColoredParticle(world, newLocation.clone().add(0, 0.5, 0), playerColor);

                currentStep++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LudoPlugin")), 0, 1); // Alle 1 Tick
    }

    private void spawnColoredParticle(World world, Location location, Color color) {
        world.spawnParticle(
                Particle.DUST, // Alternativer Partikeltyp
                location,
                0, // Anzahl der Partikel
                color.getRed() / 255.0, // Rotanteil (0.0 - 1.0)
                color.getGreen() / 255.0, // Grünanteil (0.0 - 1.0)
                color.getBlue() / 255.0, // Blauanteil (0.0 - 1.0)
                1 // Partikelgröße
        );
    }
}
