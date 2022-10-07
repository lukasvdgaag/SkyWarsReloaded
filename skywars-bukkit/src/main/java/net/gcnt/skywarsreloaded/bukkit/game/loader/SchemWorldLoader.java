package net.gcnt.skywarsreloaded.bukkit.game.loader;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import io.papermc.lib.PaperLib;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWChunkGenerator;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.utils.FileUtils;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SchemWorldLoader extends BukkitWorldLoader {

    private final BukkitSkyWarsReloaded plugin;

    public SchemWorldLoader(BukkitSkyWarsReloaded pluginIn) {
        super(pluginIn);
        this.plugin = pluginIn;

    }

    @Override
    public CompletableFuture<Boolean> generateWorldInstance(LocalGameInstance gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.createEmptyWorld(gameWorld).thenRun(() -> {
            plugin.getScheduler().runSync(() -> postWorldGenerateTask(gameWorld, future));
        });
        return future;
    }

    protected void postWorldGenerateTask(LocalGameInstance gameWorld, CompletableFuture<Boolean> future) {
        boolean res = false;
        try {
            res = pasteTemplateSchematic(gameWorld).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        future.complete(res);
    }

    @Override
    public CompletableFuture<Void> createEmptyWorld(LocalGameInstance gameWorld) {
        WorldCreator creator = new WorldCreator(gameWorld.getWorldName());
        creator.generateStructures(false);
        creator.type(WorldType.FLAT);

        // Apply generator settings based on the MC version
        String generatorSettings = this.plugin.getNMSManager().getNMS().getVoidGeneratorSettings();
        creator.generatorSettings(generatorSettings);

        // Override world generator
        creator.generator(((BukkitSWChunkGenerator) this.plugin.getNMSManager().getNMS().getChunkGenerator()).getGenerator());

        World createdWorld = creator.createWorld();
        assert createdWorld != null;
        CompletableFuture<Void> future = new CompletableFuture<>();

        // This won't do anything in 1.11 or lower -> will be laggier
        PaperLib.getChunkAtAsync(createdWorld.getSpawnLocation()).thenAccept(chunk -> {
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
    public CompletableFuture<Boolean> pasteTemplateSchematic(LocalGameInstance gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> futureFail = CompletableFuture.completedFuture(false);
        // todo: Later make this work with FAWE
        CompletableFuture<Boolean> futureOk = CompletableFuture.completedFuture(true);

        File schemFolder = new File(plugin.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER.toString());
        String schemFileName = gameWorld.getTemplate().getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (!schemFile.exists()) {
            return futureFail;
        }

        Clipboard clip = plugin.getSchematicManager().getSchematic(schemFolder, schemFileName);
        if (clip == null) {
            return futureFail; // todo throw error?
        }

        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) {
            throw new IllegalStateException(String.format(
                    "GameWorld %s$ doesn't have a valid minecraft world. Check the console for other errors!",
                    gameWorld.getId()
            ));
        }

        // The returned EditSession is already auto-closed using a try-w/ statement inside pasteSchematic()
        //noinspection resource
        plugin.getSchematicManager().pasteSchematic(clip, new BukkitWorld(world), BlockVector3.at(0, 0, 0), true);
        return futureOk;
    }

    @Override
    public void deleteWorldInstance(LocalGameInstance gameWorld) {
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
    public void createBasePlatform(LocalGameInstance gameWorld) {
        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) return;

        world.getBlockAt(
                InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y,
                InternalProperties.MAP_CREATE_PLATFORM_Z
        ).setType(Material.STONE);
    }

    @Override
    public CompletableFuture<Boolean> save(LocalGameInstance gameWorld) {
        boolean successful = plugin.getSchematicManager().saveGameWorldToSchematic(gameWorld, plugin.getUtils()
                .getWorldEditWorld(gameWorld.getWorldName()));
        return CompletableFuture.completedFuture(successful);
    }
}
