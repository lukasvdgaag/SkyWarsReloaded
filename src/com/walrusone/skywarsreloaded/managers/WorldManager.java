package com.walrusone.skywarsreloaded.managers;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.api.NMS;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class WorldManager
{
  public WorldManager() {}
  
  public World createEmptyWorld(String name, Environment environment)
  {
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
    SkyWarsReloaded.getNMS().setGameRule(world, "mobGriefing", "false");
    SkyWarsReloaded.getNMS().setGameRule(world, "doFireTick", "false");
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
  
  public void unloadWorld(String w) {
    World world = SkyWarsReloaded.get().getServer().getWorld(w);

    if (world != null) {
        for (Player p : world.getPlayers()) {
            p.teleport(SkyWarsReloaded.getCfg().getSpawn());
        }
      SkyWarsReloaded.get().getServer().unloadWorld(world, false);
    }
  }
  
  public void copyWorld(File source, File target) {
    try {
      ArrayList<String> ignore = new ArrayList(java.util.Arrays.asList(new String[] { "uid.dat", "session.dat", "session.lock" }));
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
          Object out = new java.io.FileOutputStream(target);
          byte[] buffer = new byte['Ѐ'];
          int length;
          while ((length = in.read(buffer)) > 0)
            ((OutputStream)out).write(buffer, 0, length);
          in.close();
          ((OutputStream)out).close();
        }
      }
    } catch (java.io.IOException e) {
      SkyWarsReloaded.get().getLogger().info("Failed to copy world as required!");
    }
  }
  
  public void deleteWorld(String name) {
    unloadWorld(name);
    File target = new File(SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath(), name);
    deleteWorld(target);
  }
  
  public void deleteWorld(File path)
  {
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
