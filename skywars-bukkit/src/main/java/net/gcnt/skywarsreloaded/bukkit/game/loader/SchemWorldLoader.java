package net.gcnt.skywarsreloaded.bukkit.game.loader;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import io.papermc.lib.PaperLib;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameWorld;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.FileUtils;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public CompletableFuture<Boolean> generateWorldInstance(GameWorld gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.createEmptyWorld(gameWorld).thenRun(() -> plugin.getScheduler().runSync(() -> postWorldGenerateTask(gameWorld, future)));
        return future;
    }

    protected void postWorldGenerateTask(GameWorld gameWorld, CompletableFuture<Boolean> future) {
        boolean res = false;
        try {
            res = pasteTemplateSchematic(gameWorld).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        future.complete(res);
    }

    @Override
    public CompletableFuture<Void> createEmptyWorld(GameWorld gameWorld) {
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
        PaperLib.getChunkAtAsync(createdWorld.getSpawnLocation()).thenAccept(chunk -> {
                    chunk.addPluginChunkTicket(plugin.getBukkitPlugin());
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
    public CompletableFuture<Boolean> pasteTemplateSchematic(GameWorld gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> futureFail = CompletableFuture.completedFuture(false);
        // todo: Later make this work with FAWE
        CompletableFuture<Boolean> futureOk = CompletableFuture.completedFuture(true);

        File schemFolder = new File(plugin.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER.toString());
        String schemFileName = gameWorld.getTemplate().getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (!schemFile.exists()) return futureFail;

        Clipboard clip = plugin.getSchematicManager().getSchematic(schemFolder, schemFileName);
        if (clip == null) return futureFail; // todo throw error?

        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) {
            throw new IllegalStateException(String.format(
                    "GameWorld %s$ doesn't have a valid minecraft world. Check the console for other errors!",
                    gameWorld.getId()
            ));
        }

        plugin.getSchematicManager().pasteSchematic(clip, new BukkitWorld(world), BlockVector3.at(0, 0, 0), true);
        return futureOk;
    }

    @Override
    public void deleteWorldInstance(GameWorld gameWorld) {
        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) {
            return;
        }

        final SWCoord loc = plugin.getDataConfig().getCoord("lobby");
        final Location bukkitLoc = (loc != null && loc.world() != null) ? new Location(Bukkit.getWorld(loc.world().getName()), loc.xPrecise(), loc.yPrecise(), loc.zPrecise(), loc.yaw(), loc.pitch()) : new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

        for (Player player : world.getPlayers()) {
            player.teleport(bukkitLoc);
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
    public void createBasePlatform(GameWorld gameWorld) {
        World world = ((BukkitGameWorld) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getBlockAt(
                InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y,
                InternalProperties.MAP_CREATE_PLATFORM_Z
        ).setType(Material.STONE);
    }

    @Override
    public CompletableFuture<Boolean> save(GameWorld gameWorld) {
        boolean successful = plugin.getSchematicManager().saveGameWorldToSchematic(gameWorld, plugin.getUtils()
                .getWorldEditWorld(gameWorld.getWorldName()));
        return CompletableFuture.completedFuture(successful);
    }
}
