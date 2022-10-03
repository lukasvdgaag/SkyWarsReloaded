package net.gcnt.skywarsreloaded.bukkit.wrapper.world;

import net.gcnt.skywarsreloaded.wrapper.world.SWChunkGenerator;
import org.bukkit.generator.ChunkGenerator;

public class BukkitSWChunkGenerator implements SWChunkGenerator {

    private final ChunkGenerator generator;

    public BukkitSWChunkGenerator(ChunkGenerator generator) {
        this.generator = generator;
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }
}
