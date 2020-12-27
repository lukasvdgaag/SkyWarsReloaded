package com.walrusone.skywarsreloaded.managers.worlds;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;

public class FileWorldManager implements WorldManager {

    public World createEmptyWorld(String name, Environment environment) {
        if (org.bukkit.Bukkit.getWorld(name) == null) {
            loadWorld(name, environment);
            return org.bukkit.Bukkit.getWorld(name);
        }
        return null;
    }

    public boolean loadWorld(String worldName, Environment environment) {
        WorldCreator worldCreator = new WorldCreator(worldName);
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
        SkyWarsReloaded.getNMS().setGameRule(world, "mobGriefing", "true");
        SkyWarsReloaded.getNMS().setGameRule(world, "doFireTick", "true");
        SkyWarsReloaded.getNMS().setGameRule(world, "showDeathMessages", "false");
        SkyWarsReloaded.getNMS().setGameRule(world, "announceAdvancements", "false");

        boolean loaded = false;
        for (World w : SkyWarsReloaded.get().getServer().getWorlds()) {
            if (w.getName().equals(world.getName())) {
                loaded = true;
                break;
            }
        }
        return loaded;
    }

    public void unloadWorld(String w, boolean save) {
        World world = SkyWarsReloaded.get().getServer().getWorld(w);

        if (world != null) {
            for (Player p : world.getPlayers()) {
                p.teleport(SkyWarsReloaded.getCfg().getSpawn());
            }
            SkyWarsReloaded.get().getServer().unloadWorld(world, save);
        }
    }

    public void copyWorld(File source, File target) {
        try {
            List<String> ignore = Lists.newArrayList("uid.dat", "session.dat", "session.lock");
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if ((!target.exists()) &&
                            (target.mkdirs())) {
                        String[] files = source.list();
                        if (files != null) {
                            for (String file : files) {
                                File srcFile = new File(source, file);
                                File destFile = new File(target, file);
                                copyWorld(srcFile, destFile);
                            }
                        }
                    }
                } else {
                    java.io.InputStream in = new java.io.FileInputStream(source);
                    OutputStream out = new java.io.FileOutputStream(target);
                    byte[] buffer = new byte['Ѐ'];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (FileNotFoundException e) {
            SkyWarsReloaded.get().getLogger().log(Level.SEVERE, "Failed to copy world as required! - file not found");
            e.printStackTrace();
        } catch (IOException e) {
            SkyWarsReloaded.get().getLogger().info("Failed to copy world as required!");
            e.printStackTrace();
        }
    }

    public void deleteWorld(String name, boolean removeFile) {
        unloadWorld(name, false);
        File target = new File(SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath(), name);
        deleteWorld(target);
    }

    public void deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorld(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        path.delete();
    }
}
