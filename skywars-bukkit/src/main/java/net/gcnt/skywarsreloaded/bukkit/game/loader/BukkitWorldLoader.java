package net.gcnt.skywarsreloaded.bukkit.game.loader;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.loader.AbstractWorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class BukkitWorldLoader extends AbstractWorldLoader {

    public BukkitWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public void generateWorld(GameWorld gameWorld) {
        createEmptyWorld(gameWorld);
    }

    @Override
    public void createEmptyWorld(GameWorld gameWorld) {
        WorldCreator creator = new WorldCreator(gameWorld.getWorldName());
        creator.generateStructures(false);
        creator.generator(new ChunkGenerator() {
            @Override
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                ChunkData chunkData = createChunkData(world);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        biome.setBiome(i, j, org.bukkit.block.Biome.valueOf("VOID"));
                    }
                }
                return chunkData;
            }
        });
        creator.createWorld();
    }

    @Override
    public void loadSchematic(GameWorld gameWorld) {
        Clipboard clip = plugin.getSchematicManager().getSchematic(new File(plugin.getDataFolder(), "worlds"), gameWorld.getTemplate().getName() + ".schem");
        if (clip == null) return; // todo throw error?
        World world = Bukkit.getWorld(gameWorld.getTemplate().getName());
        if (world == null) return; // todo throw error? regenerate world?

        plugin.getSchematicManager().pasteSchematic(clip, new BukkitWorld(world), BlockVector3.at(0, 0, 0));
    }

    @Override
    public void deleteWorld(GameWorld gameWorld) {
        World world = Bukkit.getWorld(gameWorld.getWorldName());
        if (world == null) return;

        Bukkit.unloadWorld(world, false);
        try {
            Files.deleteIfExists(world.getWorldFolder().toPath());
        } catch (IOException e) {
            plugin.getLogger().error(String.format("Failed to delete world %s. (%s)", world.getName(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }
}
