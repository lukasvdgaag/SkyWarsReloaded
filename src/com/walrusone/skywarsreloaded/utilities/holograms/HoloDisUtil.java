package com.walrusone.skywarsreloaded.utilities.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HoloDisUtil extends HologramsUtil {

    private static HashMap<LeaderType, HashMap<String, ArrayList<Hologram>>> holograms = new HashMap<>();

    public HoloDisUtil() {
        getFC();
    }

    @Override
    public void createLeaderHologram(Location loc, LeaderType type, String formatKey) {
        Hologram hologram = HologramsAPI.createHologram(SkyWarsReloaded.get(), loc);
        holograms.computeIfAbsent(type, k -> new HashMap<>());
        holograms.get(type).computeIfAbsent(formatKey, k -> new ArrayList<>());
        holograms.get(type).get(formatKey).add(hologram);
        if (fc == null) {
            getFC();
        }
        if (fc != null) {
            List<String> locs = new ArrayList<>();
            for (Hologram holo : holograms.get(type).get(formatKey)) {
                locs.add(Util.get().locationToString(holo.getLocation()));
            }
            fc.set("leaderboard." + type.toString().toLowerCase() + "." + formatKey + ".locations", locs);
            try {
                fc.save(holoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateLeaderHolograms(type);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void updateLeaderHolograms(LeaderType type) {
        if (SkyWarsReloaded.get().serverLoaded()) {
            holograms.computeIfAbsent(type, k -> new HashMap<>());
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                if (fc == null) {
                    getFC();
                }
                if (fc != null) {
                    for (String key : holograms.get(type).keySet()) {
                        for (Hologram hologram : holograms.get(type).get(key)) {
                            hologram.clearLines();
                            List<String> format = fc.getStringList("leaderboard." + type.toString().toLowerCase() + "." + key + ".format");
                            for (int i = 0; i < format.size(); i++) {
                                String line = getFormattedString(format.get(i), type);
                                if (line.startsWith("item:")) {
                                    ItemStack item = null;
                                    String mat = line.substring(5);
                                    if (mat.contains("playerhead")) {
                                        String num = mat.substring(12, mat.length() - 1);
                                        if (Util.get().isInteger(num) && SkyWarsReloaded.getLB().getTopList(type) != null && SkyWarsReloaded.getLB().getTopList(type).size() > Integer.valueOf(num) - 1) {
                                            Player player = Bukkit.getPlayer(SkyWarsReloaded.getLB().getTopList(type).get(Integer.valueOf(num) - 1).getUUID());
                                            if (player != null) {
                                                if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                                                    item = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
                                                } else {
                                                    item = new ItemStack(Material.valueOf("PLAYER_HEAD"), 1);
                                                }
                                                SkullMeta meta1 = (SkullMeta) item.getItemMeta();
                                                SkyWarsReloaded.getNMS().updateSkull(meta1, player);
                                                meta1.setDisplayName(ChatColor.YELLOW + player.getName());
                                                item.setItemMeta(meta1);
                                            }
                                        }
                                    } else {
                                        Material material = Material.matchMaterial(mat);
                                        if (material != null) {
                                            item = new ItemStack(material, 1);
                                        }
                                    }
                                    if (item != null) {
                                        hologram.insertItemLine(i, item);
                                    } else {
                                        hologram.insertTextLine(i, "  ");
                                    }
                                } else {
                                    hologram.insertTextLine(i, ChatColor.translateAlternateColorCodes('&', line));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean removeHologram(Location loc) {
        Hologram hologram = null;
        LeaderType typeToRemove = null;
        String keyToRemove = null;
        double distance = 1000000;
        for (LeaderType type : LeaderType.values()) {
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                if (fc.getConfigurationSection("leaderboard." + type.toString().toLowerCase()) != null) {
                    for (String key : fc.getConfigurationSection("leaderboard." + type.toString().toLowerCase()).getKeys(false)) {
                        if (holograms.get(type) != null && holograms.get(type).get(key) != null) {
                            for (Hologram holo : holograms.get(type).get(key)) {
                                if (loc.distance(holo.getLocation()) < distance) {
                                    hologram = holo;
                                    typeToRemove = type;
                                    keyToRemove = key;
                                    distance = loc.distance(holo.getLocation());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (hologram != null) {
            holograms.get(typeToRemove).get(keyToRemove).remove(hologram);
            hologram.delete();
            if (fc != null) {
                List<String> locs = new ArrayList<>();
                for (Hologram holo : holograms.get(typeToRemove).get(keyToRemove)) {
                    locs.add(Util.get().locationToString(holo.getLocation()));
                }
                fc.set("leaderboard." + typeToRemove.toString().toLowerCase() + "." + keyToRemove + ".locations", locs);
                try {
                    fc.save(holoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}