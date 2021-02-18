package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GhastEvent extends MatchEvent {
    private ArrayList<Entity> mobsSpawned = new ArrayList();
    private BukkitTask br1;
    private BukkitTask br2;

    public GhastEvent(GameMap map, boolean b) {
        gMap = map;
        enabled = b;
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            eventName = "GhastEvent";
            slot = 17;
            material = new org.bukkit.inventory.ItemStack(Material.GHAST_TEAR, 1);
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

    public void doEvent() {
        if (gMap.getMatchState() == MatchState.PLAYING) {
            fired = true;
            sendTitle();
            CoordLoc loc = gMap.getSpectateSpawn();
            final World world = gMap.getCurrentWorld();
            Location location = new Location(world, loc.getX(), loc.getY(), loc.getZ());


            br1 = new BukkitRunnable() {
                public void run() {
                    for (int i = 0; i < gMap.getTeamCards().size(); i++) {
                        Location loc = new Location(gMap.getCurrentWorld(), ((TeamCard) gMap.getTeamCards().get(i)).getSpawns().get(0).getX(), ((TeamCard) gMap.getTeamCards().get(i)).getSpawns().get(0).getY(), ((TeamCard) gMap.getTeamCards().get(i)).getSpawns().get(0).getZ());
                        Location spawn;
                        do {
                            spawn = loc.clone().add(0.0D, 10.0D, 0.0D);
                        } while (!loc.getBlock().getType().equals(Material.AIR));
                        LivingEntity ent = (LivingEntity) world.spawnEntity(spawn, org.bukkit.entity.EntityType.GHAST);
                        mobsSpawned.add(ent);
                    }

                }

            }.runTaskLater(SkyWarsReloaded.get(), 3L);
            if (length != -1) {


                br2 = new BukkitRunnable() {
                    public void run() {
                        endEvent(false);
                    }
                }.runTaskLater(SkyWarsReloaded.get(), length * 20L);
            }
        }
    }

    public void endEvent(boolean force) {
        if (fired) {
            if (force) {
                br1.cancel();
                if (length != -1) {
                    br2.cancel();
                }
            }
            for (Entity ent : mobsSpawned) {
                if ((ent != null) && (!ent.isDead())) {
                    ent.remove();
                }
            }
            mobsSpawned.clear();
            if (gMap.getMatchState() == MatchState.PLAYING) {
                MatchManager.get().message(gMap, ChatColor.translateAlternateColorCodes('&', endMessage));
            }
            if ((repeatable) || (force)) {
                setStartTime();
                startTime += gMap.getTimer();
                fired = false;
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
            try {
                fc.save(mapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
