package com.walrusone.skywarsreloaded.managers.worlds;

import com.grinderwolf.swm.plugin.SWMPlugin;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import com.grinderwolf.swm.plugin.config.WorldsConfig;
import com.infernalsuite.aswm.api.SlimeNMSBridge;
import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.SlimeWorldInstance;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

@SuppressWarnings({"CallToPrintStackTrace", "unused"})
public class ASPWorldManagerImpl implements ASPWorldManager {

    SlimePlugin plugin;
    SlimeLoader loader;
    @SuppressWarnings("UnstableApiUsage")
    SlimeNMSBridge slimeNMS;

    public ASPWorldManagerImpl() {
        this.plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        this.loader = plugin.getLoader(SkyWarsReloaded.getCfg().getSlimeWorldManagerSource());
        // Attempt to get slime nms
        try {
            Class<SWMPlugin> swmPluginClass = SWMPlugin.class;
            Field field = swmPluginClass.getDeclaredField("BRIDGE_INSTANCE");
            field.setAccessible(true);
            // noinspection UnstableApiUsage
            this.slimeNMS = (SlimeNMSBridge) field.get(null);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            slimeNMS = null;
            e.printStackTrace();
        }
    }

    @Override
    public World createEmptyWorld(String name, World.Environment environment) {
        WorldData worldData = new WorldData();
        SlimePropertyMap propertyMap = worldData.toPropertyMap();
        propertyMap.setValue(SlimeProperties.SPAWN_X, 0);
        propertyMap.setValue(SlimeProperties.SPAWN_Y, 64);
        propertyMap.setValue(SlimeProperties.SPAWN_Z, 0);
        propertyMap.setValue(SlimeProperties.ENVIRONMENT, environment.name());
        propertyMap.setValue(SlimeProperties.DIFFICULTY, "normal");

        try {
            SlimeWorld slimeWorld = plugin.createEmptyWorld(loader, name, false, propertyMap);

            plugin.loadWorld(slimeWorld);

            World bukkitWorld = Bukkit.getWorld(name);
            if (bukkitWorld == null) return null;

            Bukkit.getPluginManager().callEvent(new WorldLoadEvent(bukkitWorld));

            Location location = new Location(bukkitWorld, 0, 61, 0);
            location.getBlock().setType(Material.BEDROCK);

            WorldsConfig config = ConfigManager.getWorldConfig();
            config.getWorlds().put(name, worldData);
            config.save();

            //if (world == null) return null;

            bukkitWorld.setDifficulty(org.bukkit.Difficulty.NORMAL);
            bukkitWorld.setSpawnFlags(true, true);
            bukkitWorld.setPVP(true);
            bukkitWorld.setStorm(false);
            bukkitWorld.setThundering(false);
            bukkitWorld.setWeatherDuration(Integer.MAX_VALUE);
            bukkitWorld.setKeepSpawnInMemory(false);
            bukkitWorld.setTicksPerSpawns(SpawnCategory.ANIMAL, 1);
            bukkitWorld.setTicksPerSpawns(SpawnCategory.MONSTER, 1);
            bukkitWorld.setAutoSave(false);

            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "doMobSpawning", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "mobGriefing", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "doFireTick", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "showDeathMessages", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "announceAdvancements", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "doDaylightCycle", "false");

            return bukkitWorld;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean loadWorld(String worldName, World.Environment environment, boolean readOnly) {

        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#loadWorld plugin: " + plugin);
            SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#loadWorld worldName: " + worldName);
        }

        WorldsConfig config = ConfigManager.getWorldConfig();
        WorldData worldData = config.getWorlds().get(worldName);

        if (worldData == null) {
            SkyWarsReloaded.get().getLogger().severe("An error occurred while loading \"" + worldName + "\" from SlimeWorldManager. Does the map exist?");
            return false;
        }

        // Test if the world already exists in memory
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            // Update settings for world
            this.setWorldSettings(world);
            // Unload world
            SkyWarsReloaded.get().getServer().unloadWorld(world, false);
            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#loadWorld unloaded world");
            }
        }

        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#loadWorld worldData: " + worldData);
        }

        // If world not already loaded, we fetch & load world from SWM
        try {
            SlimeWorld slimeWorld = plugin.loadWorld(loader, worldName, readOnly || worldData.isReadOnly(), worldData.toPropertyMap());
            plugin.loadWorld(slimeWorld);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Second attempt successful?
        world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Something went wrong whilst loading a world for the arena " + worldName + ". World is null.");
            return false;
        }

        // If all succeeds, set world settings and finish
        this.setWorldSettings(world);
        return true;
    }

    @Override
    public void unloadWorld(String worldName, boolean shouldSave) {
        // Pre vars
        WorldsConfig config = ConfigManager.getWorldConfig();
        WorldData worldData = config.getWorlds().get(worldName);
        World world = Bukkit.getWorld(worldName);

        // Pre check
        if (world == null) {
            SkyWarsReloaded.get().getLogger().severe("World " + worldName + " is not loaded but was attempted to be unloaded anyway!");
            return;
        }

        // Execute unload
        try {
            // In the case the world already exits in SWM
            if (loader.worldExists(worldName)) {
                // noinspection UnstableApiUsage
                SlimeWorldInstance slimeWorld = slimeNMS.getInstance(world);
                if (slimeWorld == null) {
                    SkyWarsReloaded.get().getLogger().severe(
                            "Cannot save a world to SWM's storage if that world already exists in SWM's storage " +
                                    "but wasn't loaded from SWM originally!");
                    return;
                }
//                // If we should save but it's not in write mode already
//                // (UNSAFE OPERATION! IGNORES LOCKS, DOES NOT RETRY IF IT FAILS)
//                if (shouldSave && slimeWorld.isReadOnly()) {
//                    // Info
//                    SkyWarsReloaded.get().getLogger().warning(
//                            "SWM has this world in read-only mode and skywars was instructed to save " +
//                                    "this world anyway. Changes should not be lost since skywars will save the " +
//                                    "world manually, however please disable read-only mode before making edits." +
//                                    "Force saving worlds is not safe! (Issues caused by not removing read-only " +
//                                    "mode are under your responsibility)");
//
//                    // Save current world data
//                    byte[] serializedWorld = this.serializeSlimeWorld(slimeWorld);
//
//                    // Manually write to loader
//                    loader.saveWorld(worldName, serializedWorld);
//
//                    // Unload without saving, since we already forced that.
//                    Bukkit.unloadWorld(world, false);
//                }
//                else {
                    // Save if requested, otherwise just unload
                    Bukkit.unloadWorld(world, shouldSave);
//                }
            }
            else {
                // In the case the world doesn't already exist in the loader specified in SWM, import it.
                Bukkit.unloadWorld(world, shouldSave);
                if (shouldSave) {
                    plugin.importWorld(world.getWorldFolder(), worldName, loader);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    protected byte[] serializeSlimeWorld(SlimeWorldInstance slimeWorldInstance) throws IllegalStateException {
//        try {
//            return SlimeSerializer
//            return ((SlimeLoadedWorld) slimeWorld).serialize().get();
//        } catch (IndexOutOfBoundsException e) {
//            throw new IllegalStateException(slimeWorld.getName() + " is too big!", e);
//        } catch (IOException | InterruptedException | ExecutionException e) {
//            throw new IllegalStateException("Couldn't serialize world " + slimeWorld.getName(), e);
//        }
//    }

    @Override
    public WorldManagerType getType() {
        return WorldManagerType.ASWM;
    }

    @Override
    public void copyWorld(File source, File target) {

    }

    @Override
    public void deleteWorld(String name, boolean removeFile) {
        unloadWorld(name, false);
        if (removeFile) {
            try {
                loader.deleteWorld(name);
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong whilst deleting a world for the arena " + name);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteWorld(File file) {

    }

    // UTILS

    public void setWorldSettings(World world) {
        world.setSpawnFlags(true, true);
        world.setPVP(true);
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setKeepSpawnInMemory(false);
        world.setTicksPerAnimalSpawns(1);
        world.setTicksPerMonsterSpawns(1);
        world.setAutoSave(false);

        SkyWarsReloaded.getNMS().setGameRule(world, "doMobSpawning", "false");
        SkyWarsReloaded.getNMS().setGameRule(world, "mobGriefing", "false");
        SkyWarsReloaded.getNMS().setGameRule(world, "doFireTick", "false");
        SkyWarsReloaded.getNMS().setGameRule(world, "showDeathMessages", "false");
        SkyWarsReloaded.getNMS().setGameRule(world, "announceAdvancements", "false");
        SkyWarsReloaded.getNMS().setGameRule(world, "doDaylightCycle", "false");
    }
}
