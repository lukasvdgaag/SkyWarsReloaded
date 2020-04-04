package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class DeathMatchEvent extends MatchEvent {

    public DeathMatchEvent(GameMap map, boolean b) {
        this.gMap = map;
        this.enabled = b;
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            eventName = "DeathMatchEvent";
            slot = 8;
            material = new ItemStack(Material.DIAMOND_SWORD, 1);
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
            Collections.shuffle(gMap.getDeathMatchSpawns());
            int delay = 0;
            for (int i = 0; i < gMap.getAlivePlayers().size(); i++) {
                int spawn;
                if (i < gMap.getDeathMatchSpawns().size()) {
                    spawn = i;
                } else {
                    spawn = 0;
                }
                CoordLoc cLoc = gMap.getDeathMatchSpawns().get(spawn);
                Location loc = new Location(gMap.getCurrentWorld(), cLoc.getX(), cLoc.getY(), cLoc.getZ());
                loc.getChunk().load(true);
                Player player = gMap.getAlivePlayers().get(i);
                delay = i * 2;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            player.teleport(loc.add(0, 2, 0), TeleportCause.END_PORTAL);
                            gMap.addDeathMatchWaiter(player);
                        }
                    }
                }.runTaskLater(SkyWarsReloaded.get(), delay);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    sendTitle();
                }
            }.runTaskLater(SkyWarsReloaded.get(), 2 + delay);

            new BukkitRunnable() {
                @Override
                public void run() {
                    gMap.clearDeathMatchWaiters();
                    for (final Player player : gMap.getAlivePlayers()) {
                        if (SkyWarsReloaded.getCfg().titlesEnabled()) {
                            Util.get().sendTitle(player, 2, 20, 2, ChatColor.translateAlternateColorCodes('&', endMessage),
                                    ChatColor.translateAlternateColorCodes('&', ""));
                        }
                    }
                    endEvent(false);
                }
            }.runTaskLater(SkyWarsReloaded.get(), 60 + delay);
        }
    }

    @Override
    public void endEvent(boolean force) {
        if (fired) {
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