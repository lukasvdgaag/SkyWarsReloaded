package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class ArrowRainEvent
        extends MatchEvent {
    public int count;
    private int per2Tick;
    private BukkitTask br;

    public ArrowRainEvent(GameMap map, boolean b) {
        gMap = map;
        enabled = b;
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            eventName = "ArrowRainEvent";
            slot = 2;
            material = new ItemStack(Material.ARROW, 1);
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
            per2Tick = fc.getInt("events." + eventName + ".spawnPer2Tick");
        }
    }

    public void doEvent() {
        if (gMap.getMatchState() == MatchState.PLAYING) {
            fired = true;
            sendTitle();
            final World world = gMap.getCurrentWorld();


            br = new BukkitRunnable() {
                public void run() {
                    if ((length != -1) &&
                            (count / 10 >= length)) {
                        endEvent(false);
                    }

                    if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                        for (Player player : gMap.getAlivePlayers()) {
                            if (player != null) {
                                Location loc = player.getLocation();
                                for (int i = 0; i < per2Tick; i++) {
                                    world.spawnEntity(loc.add(ThreadLocalRandom.current().nextDouble(-3.0D, 3.0D), ThreadLocalRandom.current().nextDouble(10.0D, 20.0D), ThreadLocalRandom.current().nextDouble(-3.0D, 3.0D)), EntityType.ARROW);
                                }
                            }
                        }
                    } else {
                        endEvent(false);
                    }
                    count += 1;
                }
            }.runTaskTimer(SkyWarsReloaded.get(), 0L, 2L);
        }
    }

    public void endEvent(boolean force) {
        if (fired) {
            br.cancel();
            if (gMap.getMatchState() == MatchState.PLAYING) {
                MatchManager.get().message(gMap, ChatColor.translateAlternateColorCodes('&', endMessage));
            }
            if ((repeatable) || (force)) {
                setStartTime();
                startTime += gMap.getTimer();
                fired = false;
                count = 0;
            }
        }
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
            fc.set("events." + eventName + ".spawnPer2Tick", (per2Tick));
            try {
                fc.save(mapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
