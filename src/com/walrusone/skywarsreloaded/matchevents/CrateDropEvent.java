package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class CrateDropEvent extends MatchEvent {
    private int maxNumOfCrates;
    private int maxItemsPerCrate;
    private BukkitTask br;

    public CrateDropEvent(GameMap map, boolean b) {
        gMap = map;
        enabled = b;
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            eventName = "CrateDropEvent";
            slot = 6;
            material = new org.bukkit.inventory.ItemStack(Material.ENDER_CHEST, 1);
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
            maxItemsPerCrate = fc.getInt("events." + eventName + ".maxItemsPerCrate");
            maxNumOfCrates = fc.getInt("events." + eventName + ".maxNumOfCrates");
        }
    }

    public void doEvent() {
        if (gMap.getMatchState() == MatchState.PLAYING) {
            fired = true;
            sendTitle();
            if (addCrates() < 1) {


                new BukkitRunnable() {
                    public void run() {
                        CrateDropEvent.this.addCrates();
                    }
                }.runTaskLater(SkyWarsReloaded.get(), 10L);
            }
            if (length != -1) {


                br = new BukkitRunnable() {
                    public void run() {
                        endEvent(false);
                    }
                }.runTaskLater(SkyWarsReloaded.get(), length * 20L);
            }
        }
    }

    public void endEvent(boolean force) {
        if (fired) {
            if ((force) &&
                    (length != -1)) {
                br.cancel();
            }

            if (gMap.getMatchState() == MatchState.PLAYING) {
                MatchManager.get().message(gMap, org.bukkit.ChatColor.translateAlternateColorCodes('&', endMessage));
            }
            gMap.removeCrates();
            if ((repeatable) || (force)) {
                setStartTime();
                startTime += gMap.getTimer();
                fired = false;
            }
        }
    }

    private int addCrates() {
        int spawned = 0;
        World world = gMap.getCurrentWorld();
        int cratesToAdd = Util.get().getRandomNum(0, maxNumOfCrates);
        for (int i = 0; i < cratesToAdd; i++) {
            Location loc = new Location(world, gMap.getSpectateSpawn().getX(), 0.0D, gMap.getSpectateSpawn().getZ());
            Location loc2 = new Location(world, ((TeamCard) gMap.getTeamCards().get(0)).getSpawns().get(0).getX(), ((TeamCard) gMap.getTeamCards().get(0)).getSpawns().get(0).getY(), ((TeamCard) gMap.getTeamCards().get(0)).getSpawns().get(0).getZ());
            int distance = (int) Math.hypot(loc.getX() - loc2.getX(), loc.getZ() - loc2.getZ());
            int y = loc2.getBlockY();
            Location spawn = new Location(world, loc.getBlockX() + Util.get().getRandomNum(-distance, distance), 0.0D, loc.getBlockZ() + Util.get().getRandomNum(-distance, distance));
            Block block = world.getHighestBlockAt(spawn);
            if ((block != null) && (!block.getType().equals(Material.AIR))) {
                spawn = block.getLocation();
            } else {
                int range = Util.get().getRandomNum(y - 15, y + 15);
                spawn = new Location(world, spawn.getBlockX(), range, spawn.getBlockZ());
                world.getBlockAt(spawn).setType(Material.COBBLESTONE);
            }
            gMap.addCrate(spawn, maxItemsPerCrate);
            spawned++;
        }
        return spawned;
    }

    public void saveEventData() {
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
            fc.set("events." + eventName + ".enabled", (enabled));
            fc.set("events." + eventName + ".minStart", (min));
            fc.set("events." + eventName + ".maxStart", (max));
            fc.set("events." + eventName + ".length", (length));
            fc.set("events." + eventName + ".chance", (chance));
            fc.set("events." + eventName + ".title", title);
            fc.set("events." + eventName + ".subtitle", subtitle);
            fc.set("events." + eventName + ".startMessage", startMessage);
            fc.set("events." + eventName + ".endMessage", endMessage);
            fc.set("events." + eventName + ".announceTimer", (announceEvent));
            fc.set("events." + eventName + ".repeatable", (repeatable));
            fc.set("events." + eventName + ".maxItemsPerCrate", (maxItemsPerCrate));
            fc.set("events." + eventName + ".maxNumOfCrates", (maxNumOfCrates));
            try {
                fc.save(mapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
