package de.rettedasplanet.ludo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class GameBoardGenerator {

    public void generateLudoGameBoardWithAnimation(World world, Runnable onComplete) {
        int baseY = world.getHighestBlockYAt(0, 0) + 1;

        Material baseMaterial = Material.WHITE_CONCRETE;

        Queue<int[]> positionsQueue = new LinkedList<>();
        int[][] boardPositions = {
                {0, -3}, {1, -3}, {2, -3}, {3, -3}, {3, -2}, {3, -1}, {3, 0}, {3, 1}, {3, 2}, {3, 3},
                {2, 3}, {1, 3}, {0, 3}, {-1, 3}, {-2, 3}, {-3, 3}, {-3, 2}, {-3, 1}, {-3, 0}, {-3, -1},
                {-3, -2}, {-3, -3}, {-2, -3}, {-1, -3}
        };

        for (int[] pos : boardPositions) {
            positionsQueue.add(new int[]{pos[0], baseY, pos[1]});
        }

        // Animation starten
        new BukkitRunnable() {
            @Override
            public void run() {
                if (positionsQueue.isEmpty()) {
                    cancel();
                    generateStartFields(world, baseY, Material.RED_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.BLACK_CONCRETE);
                    onComplete.run();
                    return;
                }

                int[] pos = positionsQueue.poll();
                world.getBlockAt(pos[0], pos[1], pos[2]).setType(baseMaterial);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LudoPlugin")), 0, 2); // 2 Ticks Verzögerung
    }

    private void generateStartFields(World world, int baseY, Material red, Material yellow, Material green, Material black) {
        int[][] redStart = {{-4, -1}, {-4, -2}, {-5, -1}, {-5, -2}};
        int[][] yellowStart = {{4, 1}, {4, 2}, {5, 1}, {5, 2}};
        int[][] greenStart = {{1, 4}, {2, 4}, {1, 5}, {2, 5}};
        int[][] blackStart = {{-1, -4}, {-2, -4}, {-1, -5}, {-2, -5}};

        placeField(world, baseY, redStart, red);
        placeField(world, baseY, yellowStart, yellow);
        placeField(world, baseY, greenStart, green);
        placeField(world, baseY, blackStart, black);
    }

    private void placeField(World world, int baseY, int[][] positions, Material material) {
        for (int[] pos : positions) {
            int x = pos[0];
            int z = pos[1];
            world.getBlockAt(x, baseY, z).setType(material);
        }
    }
}
