package de.rettedasplanet.ludo.game;

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

        int[][] positions = {
                {0, -3}, {1, -3}, {2, -3}, {3, -3}, {3, -2}, {3, -1}, {3, 0}, {3, 1}, {3, 2}, {3, 3},
                {2, 3}, {1, 3}, {0, 3}, {-1, 3}, {-2, 3}, {-3, 3}, {-3, 2}, {-3, 1}, {-3, 0}, {-3, -1},
                {-3, -2}, {-3, -3}, {-2, -3}, {-1, -3}
        };

        for (int[] pos : positions) {
            positionsQueue.add(new int[]{pos[0], baseY, pos[1]});
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (positionsQueue.isEmpty()) {
                    cancel();
                    onComplete.run();
                    return;
                }
                int[] pos = positionsQueue.poll();
                world.getBlockAt(pos[0], pos[1], pos[2]).setType(baseMaterial);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LudoPlugin")), 0, 2);
    }
}