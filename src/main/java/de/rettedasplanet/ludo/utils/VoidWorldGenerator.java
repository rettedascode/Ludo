package de.rettedasplanet.ludo.utils;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidWorldGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        return createChunkData(world); // Leerer Chunk
    }

    public static World createVoidWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(World.Environment.NORMAL);
        creator.type(WorldType.FLAT);
        creator.generator(new VoidWorldGenerator());
        World world = creator.createWorld();

        // Weltregeln festlegen
        assert world != null;
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setSpawnFlags(false, false);
        world.setTime(6000);

        return world;
    }
}
