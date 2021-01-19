package com.walrusone.skywarsreloaded.managers.worlds;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

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
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.environment(environment);
        worldCreator.generateStructures(false);
        worldCreator.generator(SkyWarsReloaded.getNMS().getChunkGenerator());
        World world = worldCreator.createWorld();
        world.setDifficulty(org.bukkit.Difficulty.NORMAL);
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

        return world;
    }

    @Override
    public boolean loadWorld(String worldName, World.Environment environment) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setString(SlimeProperties.ENVIRONMENT, environment.name());
        properties.setString(SlimeProperties.DIFFICULTY, "normal");

        try {
            SlimeWorld sw = plugin.loadWorld(loader, worldName, true, properties);
            Bukkit.getScheduler().runTask(SkyWarsReloaded.get(), () -> plugin.generateWorld(sw));
        } catch (IOException | CorruptedWorldException | WorldInUseException | NewerFormatException | UnknownWorldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Something went wrong whilst loading a world for the arena " + worldName);
            e.printStackTrace();
            return false;
        }

        World world = Bukkit.getWorld(worldName);

        if (world==null) {
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
    public void unloadWorld(String world, boolean save) {
        World w = Bukkit.getWorld(world);
        Bukkit.getServer().unloadWorld(world, save);

        if (save) {
            try {
                plugin.importWorld(w.getWorldFolder(), world, loader);
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
