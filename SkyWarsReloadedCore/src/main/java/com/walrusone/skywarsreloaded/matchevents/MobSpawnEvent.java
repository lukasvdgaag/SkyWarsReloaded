package com.walrusone.skywarsreloaded.matchevents;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MobSpawnEvent extends MatchEvent {
    private int maxMobsPerPlayer;
    private int minMobsPerPlayer;
    private BukkitTask br1;
    private BukkitTask br2;
    private List<String> mobs = new ArrayList();
    private ArrayList<Entity> mobsSpawned = new ArrayList();

    public MobSpawnEvent(GameMap map, boolean b) {
        gMap = map;
        enabled = b;
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if ((!mapDataDirectory.exists()) && (!mapDataDirectory.mkdirs())) {
            return;
        }

        File mapFile = new File(mapDataDirectory, gMap.getName() + ".yml");
        if (mapFile.exists()) {
            eventName = "MobSpawnEvent";
            slot = 22;
            material = SkyWarsReloaded.getNMS().getMaterial("MOB_SPAWNER");
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
            minMobsPerPlayer = fc.getInt("events." + eventName + ".minMobsPerPlayer");
            maxMobsPerPlayer = fc.getInt("events." + eventName + ".maxMobsPerPlayer");
            mobs = fc.getStringList("events." + eventName + ".mobs");
        }
    }

    private static List<Block> getSpawnableBlocks(Location location) {
        List<Block> blocks = new ArrayList();
        for (int x = location.getBlockX() - 5; x <= location.getBlockX() + 5; x++) {
            for (int y = location.getBlockY() - 2; y <= location.getBlockY() + 2; y++) {
                for (int z = location.getBlockZ() - 5; z <= location.getBlockZ() + 5; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    Block above = location.getWorld().getBlockAt(x, y + 1, z);
                    if ((!block.getType().equals(Material.AIR)) && (above.getType().equals(Material.AIR)) && (above.getRelative(BlockFace.UP).getType().equals(Material.AIR))) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public void doEvent() {
        if (gMap.getMatchState() == MatchState.PLAYING) {
            fired = true;
            sendTitle();
            final World world = gMap.getCurrentWorld();
            for (int i = 0; i < gMap.getAlivePlayers().size(); i++) {
                final Player player = (Player) gMap.getAlivePlayers().get(i);


                br1 = new BukkitRunnable() {
                    public void run() {
                        if (player != null) {
                            List<Block> blocks = MobSpawnEvent.getSpawnableBlocks(player.getLocation());
                            Collections.shuffle(blocks);
                            for (int i = 0; i < Util.get().getRandomNum(minMobsPerPlayer, maxMobsPerPlayer); i++) {
                                Location spawn = ((Block) blocks.get(i)).getLocation().clone().add(0.0D, 1.0D, 0.0D);
                                LivingEntity ent = (LivingEntity) world.spawnEntity(spawn, EntityType.valueOf(((String) mobs.get(ThreadLocalRandom.current().nextInt(0, mobs.size()))).toUpperCase()));
                                if (((ent instanceof Zombie)) || ((ent instanceof Skeleton))) {
                                    ent.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET, 1));
                                }
                                SkyWarsReloaded.getNMS().setEntityTarget(ent, player);
                                mobsSpawned.add(ent);
                            }
                        }
                    }
                }.runTaskLater(SkyWarsReloaded.get(), i * 3L);
            }
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
            fc.set("events." + eventName + ".enabled", enabled);
            fc.set("events." + eventName + ".minStart", (min));
            fc.set("events." + eventName + ".maxStart", (max));
            fc.set("events." + eventName + ".length", (length));
            fc.set("events." + eventName + ".chance", (chance));
            fc.set("events." + eventName + ".title", title);
            fc.set("events." + eventName + ".subtitle", subtitle);
            fc.set("events." + eventName + ".startMessage", startMessage);
            fc.set("events." + eventName + ".endMessage", endMessage);
            fc.set("events." + eventName + ".announceTimer", (announceEvent));
            fc.set("events." + eventName + ".repeatable",(repeatable));
            fc.set("events." + eventName + ".minMobsPerPlayer", (minMobsPerPlayer));
            fc.set("events." + eventName + ".maxMobsPerPlayer", (maxMobsPerPlayer));
            fc.set("events." + eventName + ".mobs", mobs);
            try {
                fc.save(mapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
