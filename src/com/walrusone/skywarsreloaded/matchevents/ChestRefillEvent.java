package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.GameOption;
import java.io.File;
import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ChestRefillEvent extends MatchEvent
{
  public ChestRefillEvent(GameMap map, boolean b)
  {
    gMap = map;
    enabled = b;
    File dataDirectory = SkyWarsReloaded.get().getDataFolder();
    File mapDataDirectory = new File(dataDirectory, "mapsData");
    
    if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
      return;
    }
    
    File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
    if (mapFile.exists()) {
      eventName = "ChestRefillEvent";
      slot = 4;
      material = new ItemStack(Material.CHEST, 1);
      FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
      min = fc.getInt("events." + eventName + ".minStart");
      max = fc.getInt("events." + eventName + ".maxStart");
      length = fc.getInt("events." + eventName + ".length");
      chance = fc.getInt("events." + eventName + ".chance");
      title = fc.getString("events." + eventName + ".title");
      subtitle = fc.getString("events." + eventName + ".subtitle");
      startMessage = fc.getString("events." + eventName + ".startMessage");
      endMessage = fc.getString("events." + eventName + ".endMessage");
      announceEvent = fc.getBoolean("events." + eventName + ".announceTimer");
      repeatable = fc.getBoolean("events." + eventName + ".repeatable");
    }
  }
  
  public void doEvent()
  {
    if (gMap.getMatchState() == MatchState.PLAYING) {
      fired = true;
      sendTitle();
      gMap.getChestOption().completeOption();
      endEvent(false);
    }
  }
  
  public void endEvent(boolean force)
  {
    if ((fired) && (
      (repeatable) || (force))) {
      setStartTime();
      startTime += gMap.getTimer();
      fired = false;
    }
  }
  

  public void saveEventData()
  {
    File dataDirectory = SkyWarsReloaded.get().getDataFolder();
    File mapDataDirectory = new File(dataDirectory, "mapsData");
    
    if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
      return;
    }
    
    File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
    if (mapFile.exists()) {
      FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
      fc.set("events." + eventName + ".enabled", Boolean.valueOf(enabled));
      fc.set("events." + eventName + ".minStart", Integer.valueOf(min));
      fc.set("events." + eventName + ".maxStart", Integer.valueOf(max));
      fc.set("events." + eventName + ".length", Integer.valueOf(length));
      fc.set("events." + eventName + ".chance", Integer.valueOf(chance));
      fc.set("events." + eventName + ".title", title);
      fc.set("events." + eventName + ".subtitle", subtitle);
      fc.set("events." + eventName + ".startMessage", startMessage);
      fc.set("events." + eventName + ".endMessage", endMessage);
      fc.set("events." + eventName + ".announceTimer", Boolean.valueOf(announceEvent));
      fc.set("events." + eventName + ".repeatable", Boolean.valueOf(repeatable));
      try {
        fc.save(mapFile);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
