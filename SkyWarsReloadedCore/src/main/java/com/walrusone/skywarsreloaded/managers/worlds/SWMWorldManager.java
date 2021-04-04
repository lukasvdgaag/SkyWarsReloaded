package com.walrusone.skywarsreloaded.managers.worlds;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import com.grinderwolf.swm.plugin.config.WorldsConfig;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SWMWorldManager implements WorldManager {

    SlimePlugin plugin;
    SlimeLoader loader;

    public SWMWorldManager() {
        this.plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        this.loader = plugin.getLoader(SkyWarsReloaded.getCfg().getSlimeWorldManagerSource());
    }

    @Override
    public World createEmptyWorld(String name, World.Environment environment) {
        WorldData worldData = new WorldData();
        worldData.setSpawn("0, 64, 0");
        SlimePropertyMap propertyMap = worldData.toPropertyMap();
        propertyMap.setString(SlimeProperties.ENVIRONMENT, environment.name());
        propertyMap.setString(SlimeProperties.DIFFICULTY, "normal");

        try {
            SlimeWorld slimeWorld = plugin.createEmptyWorld(loader, name, false, propertyMap);

            plugin.generateWorld(slimeWorld);

            World bukkitWorld = Bukkit.getWorld(name);

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
            bukkitWorld.setTicksPerAnimalSpawns(1);
            bukkitWorld.setTicksPerMonsterSpawns(1);
            bukkitWorld.setAutoSave(false);

            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "doMobSpawning", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "mobGriefing", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "doFireTick", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "showDeathMessages", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "announceAdvancements", "false");
            SkyWarsReloaded.getNMS().setGameRule(bukkitWorld, "doDaylightCycle", "false");

            return bukkitWorld;
        } catch (WorldAlreadyExistsException | IOException e) {
            e.printStackTrace();
        }


        /*WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.environment(environment);
        worldCreator.generateStructures(false);
        worldCreator.generator(SkyWarsReloaded.getNMS().getChunkGenerator());*/
        return null;
    }

    @Override
    public boolean loadWorld(String worldName, World.Environment environment) {

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

        if (Bukkit.getWorld(worldName) != null) {
            World world = Bukkit.getWorld(worldName);
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
            return true;
        }

        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#loadWorld worldData: " + worldData);
        }

        try {
            SlimeWorld slimeWorld = plugin.loadWorld(loader, worldName, worldData.isReadOnly(), worldData.toPropertyMap());
            plugin.generateWorld(slimeWorld);
        } catch (IOException | CorruptedWorldException | WorldInUseException | NewerFormatException | UnknownWorldException e) {
            e.printStackTrace();
            return false;
        }

        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Something went wrong whilst loading a world for the arena " + worldName + ". World is null.");
            return false;
        }
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

        return true;
    }

    @Override
    public void unloadWorld(String worldName, boolean save) {
        World world = Bukkit.getWorld(worldName);
        Bukkit.getServer().unloadWorld(worldName, save);

        if (save) {
            try {
                plugin.importWorld(world.getWorldFolder(), worldName, loader);
            } catch (WorldAlreadyExistsException | InvalidWorldException | WorldLoadedException | WorldTooBigException | IOException e) {
                e.printStackTrace();
            }
        }
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
            } catch (UnknownWorldException | IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong whilst deleting a world for the arena " + name);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteWorld(File file) {

    }
}
