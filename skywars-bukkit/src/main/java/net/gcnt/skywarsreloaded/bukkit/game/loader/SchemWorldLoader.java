package net.gcnt.skywarsreloaded.bukkit.game.loader;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameWorld;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SchemWorldLoader extends BukkitWorldLoader {

    private Biome voidBiome;

    public SchemWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);

        try {
            voidBiome = Biome.valueOf("THE_VOID");
        } catch (Exception e) {
            voidBiome = Biome.valueOf("VOID");
        }

    }

    @Override
    public boolean generateWorldInstance(GameWorld gameWorld) {
        createEmptyWorld(gameWorld);
        return loadSchematic(gameWorld);
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
                        biome.setBiome(i, j, voidBiome);
                    }
                }
                return chunkData;
            }
        });
        creator.createWorld();
    }

    /**
     * Pastes the schematic into the world.
     * @param gameWorld GameWorld to paste into
     * @return true if the schematic existed
     */
    public boolean loadSchematic(GameWorld gameWorld) throws IllegalStateException, IllegalArgumentException{
        File schemFolder = new File(plugin.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER.toString());
        String schemFileName = gameWorld.getTemplate().getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (!schemFile.exists()) throw new IllegalArgumentException("Schematic file does not exist.");

        Clipboard clip = plugin.getSchematicManager().getSchematic(schemFolder, schemFileName);
        if (clip == null) return false; // todo throw error?

        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) {
            throw new IllegalStateException(String.format(
                    "GameWorld %s$ doesn't have a valid minecraft world. Check the console for other errors!",
                    gameWorld.getId()
            ));
        }

        plugin.getSchematicManager().pasteSchematic(clip, new BukkitWorld(world), BlockVector3.at(0, 0, 0), true);
        return true;
    }

    @Override
    public void deleteWorld(GameWorld gameWorld) {
        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) {
            return;
        }

        // todo teleport all players to lobby first.

        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException e) {
            plugin.getLogger().error(String.format("Failed to delete world %s. (%s)", world.getName(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }

    @Override
    public void createBasePlatform(GameWorld gameWorld) {
        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getBlockAt(0, 50, 0).setType(Material.STONE);
    }

    @Override
    public void updateWorldBorder(GameWorld gameWorld) {
        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(gameWorld.getTemplate().getBorderRadius());
    }
}
