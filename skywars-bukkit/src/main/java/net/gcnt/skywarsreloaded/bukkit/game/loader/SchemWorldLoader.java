package net.gcnt.skywarsreloaded.bukkit.game.loader;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import io.papermc.lib.PaperLib;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.utils.FileUtils;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class SchemWorldLoader extends BukkitWorldLoader {

    private final BukkitSkyWarsReloaded plugin;
    private final Biome voidBiome;

    public SchemWorldLoader(BukkitSkyWarsReloaded pluginIn) {
        super(pluginIn);
        this.plugin = pluginIn;

        final int version = pluginIn.getUtils().getServerVersion();

        if (version >= 13) voidBiome = Biome.valueOf("THE_VOID");
        else if (version >= 9) voidBiome = Biome.valueOf("VOID");
        else voidBiome = Biome.valueOf("FOREST");

    }

    @Override
    public CompletableFuture<Boolean> generateWorldInstance(GameInstance gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        System.out.println("Generating world instance for " + gameWorld.getTemplate().getName());
        this.createEmptyWorld(gameWorld).thenRun(() -> {
            System.out.println("running sync");
            plugin.getScheduler().runSync(() -> postWorldGenerateTask(gameWorld, future));
        });
        return future;
    }

    protected void postWorldGenerateTask(GameInstance gameWorld, CompletableFuture<Boolean> future) {
        System.out.println("Post world generate task for " + gameWorld.getTemplate().getName());
        boolean res = false;
        try {
            res = pasteTemplateSchematic(gameWorld).get();
            System.out.println("Result: " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("COMPLETED");
        future.complete(res);
    }

    @Override
    public CompletableFuture<Void> createEmptyWorld(GameInstance gameWorld) {
        System.out.println("Creating empty world for " + gameWorld.getTemplate().getName());
        WorldCreator creator = new WorldCreator(gameWorld.getWorldName());
        creator.generateStructures(false);
        creator.type(WorldType.FLAT);

        // Apply generator settings based on the MC version
        int version = this.plugin.getUtils().getServerVersion();
        if (version >= 16) {
            creator.generatorSettings("{ \"type\": \"minecraft:flat\", \"settings\": { \"biome\": \"minecraft:void\", \"lakes\": false, \"features\": false, \"layers\": [{ \"block\": \"minecraft:air\", \"height\": 1 }] } }");
        } else {
            creator.generatorSettings("3;minecraft:air;2");
        }

        // Override world generator
        creator.generator(new ChunkGenerator() {
            // todo check these methods on older and newer versions, and possibly use non-deprecated methods.
            @NotNull
            @Override
            public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
                ChunkData chunkData = createChunkData(world);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        biome.setBiome(x, z, voidBiome);
                    }
                }
                return chunkData;
            }
        });

        World createdWorld = creator.createWorld();
        assert createdWorld != null;
        CompletableFuture<Void> future = new CompletableFuture<>();
        // todo fix this for 1.16<
        PaperLib.getChunkAtAsync(createdWorld.getSpawnLocation()).thenAccept(chunk -> {
            if (plugin.getUtils().getServerVersion() >= 16) {
                chunk.addPluginChunkTicket(plugin.getBukkitPlugin());
            }
            System.out.println("Chunk ticket added for " + gameWorld.getWorldName());
            future.complete(null);
        });
        return future;
    }

    /**
     * Pastes the schematic into the world.
     *
     * @param gameWorld GameWorld to paste into
     * @return true if the schematic existed
     */
    public CompletableFuture<Boolean> pasteTemplateSchematic(GameInstance gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> futureFail = CompletableFuture.completedFuture(false);
        // todo: Later make this work with FAWE
        CompletableFuture<Boolean> futureOk = CompletableFuture.completedFuture(true);
        System.out.println("Pasting template schematic for " + gameWorld.getTemplate().getName());


        File schemFolder = new File(plugin.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER.toString());
        String schemFileName = gameWorld.getTemplate().getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (!schemFile.exists()) {
            System.out.println("Schematic file not found for " + gameWorld.getTemplate().getName());
            return futureFail;
        }

        Clipboard clip = plugin.getSchematicManager().getSchematic(schemFolder, schemFileName);
        if (clip == null) {
            System.out.println("Clipboard not found for " + gameWorld.getTemplate().getName());
            return futureFail; // todo throw error?
        }

        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) {
            throw new IllegalStateException(String.format(
                    "GameWorld %s$ doesn't have a valid minecraft world. Check the console for other errors!",
                    gameWorld.getId()
            ));
        }

        System.out.println("Pasting the actual schematic for " + gameWorld.getTemplate().getName());
        plugin.getSchematicManager().pasteSchematic(clip, new BukkitWorld(world), BlockVector3.at(0, 0, 0), true);
        return futureOk;
    }

    @Override
    public void deleteWorldInstance(GameInstance gameWorld) {
        if (gameWorld.getScheduler() != null) gameWorld.getScheduler().end();

        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) {
            return;
        }

        final SWCoord loc = plugin.getDataConfig().getCoord(RuntimeDataProperties.LOBBY_SPAWN.toString());

        for (SWPlayer player : gameWorld.getWorld().getAllPlayers()) {
            player.teleport(loc);
        }

        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException e) {
            plugin.getLogger().error(String.format("Failed to delete world %s. (%s)", world.getName(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }

    @Override
    public void deleteMap(GameTemplate gameTemplate, boolean forceUnloadInstances) {
        File schemFolder = new File(plugin.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER.toString());
        String schemFileName = gameTemplate.getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (schemFile.exists()) {
            try {
                FileUtils.forceDelete(schemFile);
            } catch (IOException e) {
                plugin.getLogger().error(String.format("Failed to delete schematic file %s. (%s)", schemFileName, e.getClass().getName() + ": " + e.getLocalizedMessage()));
            }
        }
    }

    @Override
    public void createBasePlatform(GameInstance gameWorld) {
        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getBlockAt(
                InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y,
                InternalProperties.MAP_CREATE_PLATFORM_Z
        ).setType(Material.STONE);
    }

    @Override
    public CompletableFuture<Boolean> save(GameInstance gameWorld) {
        boolean successful = plugin.getSchematicManager().saveGameWorldToSchematic(gameWorld, plugin.getUtils()
                .getWorldEditWorld(gameWorld.getWorldName()));
        return CompletableFuture.completedFuture(successful);
    }
}
