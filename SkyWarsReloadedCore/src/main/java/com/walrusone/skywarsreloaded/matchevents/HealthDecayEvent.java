package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class HealthDecayEvent extends MatchEvent {
    private int count;
    private BukkitTask br;

    public HealthDecayEvent(GameMap map, boolean b) {
        this.gMap = map;
        this.enabled = b;
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            eventName = "HealthDecayEvent";
            slot = 20;
            material = new ItemStack(Material.POISONOUS_POTATO, 1);
            FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
            this.min = fc.getInt("events." + eventName + ".minStart");
            this.max = fc.getInt("events." + eventName + ".maxStart");
            this.length = fc.getInt("events." + eventName + ".length");
            this.chance = fc.getInt("events." + eventName + ".chance");
            this.title = fc.getString("events." + eventName + ".title");
            this.subtitle = fc.getString("events." + eventName + ".subtitle");
            this.startMessage = fc.getString("events." + eventName + ".startMessage");
            this.endMessage = fc.getString("events." + eventName + ".endMessage");
            this.announceEvent = fc.getBoolean("events." + eventName + ".announceTimer");
            this.repeatable = fc.getBoolean("events." + eventName + ".repeatable");
        }
    }

    @Override
    public void doEvent() {
        if (gMap.getMatchState() == MatchState.PLAYING) {
            this.fired = true;
            sendTitle();
            br = new BukkitRunnable() {
                @Override
                public void run() {
                    if (length != -1) {
                        if (count >= length) {
                            endEvent(false);
                        }
                    }
                    if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                        if (gMap.getAlivePlayers().size() > 1) {
                            for (Player player : gMap.getAlivePlayers()) {
                                if (player != null) {
                                    double newHealth = player.getHealth() - 1;
                                    if (newHealth < 0) {
                                        newHealth = 0;
                                    }
                                    player.setHealth(newHealth);
                                }
                            }
                        }
                        count++;
                    } else {
                        endEvent(false);
                    }
                }
            }.runTaskTimer(SkyWarsReloaded.get(), 0, 20);
        }
    }

    @Override
    public void endEvent(boolean force) {
        if (fired) {
            br.cancel();
            if (gMap.getMatchState() == MatchState.PLAYING) {
                MatchManager.get().message(gMap, ChatColor.translateAlternateColorCodes('&', endMessage));
            }
            if (repeatable || force) {
                setStartTime();
                this.startTime = this.startTime + gMap.getTimer();
                this.fired = false;
            }
        }
    }

    @Override
    public void saveEventData() {
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
            fc.set("events." + eventName + ".enabled", this.enabled);
            fc.set("events." + eventName + ".minStart", this.min);
            fc.set("events." + eventName + ".maxStart", this.max);
            fc.set("events." + eventName + ".length", this.length);
            fc.set("events." + eventName + ".chance", this.chance);
            fc.set("events." + eventName + ".title", this.title);
            fc.set("events." + eventName + ".subtitle", this.subtitle);
            fc.set("events." + eventName + ".startMessage", this.startMessage);
            fc.set("events." + eventName + ".endMessage", this.endMessage);
            fc.set("events." + eventName + ".announceTimer", this.announceEvent);
            fc.set("events." + eventName + ".repeatable", this.repeatable);
            try {
                fc.save(mapFile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}