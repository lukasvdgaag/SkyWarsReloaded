package com.walrusone.skywarsreloaded.managers.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.managers.LeaderboardManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HolographicHologramManager extends AbstractHologramsManager {

    private final Map<LeaderType, Map<String, List<Hologram>>> holograms = new HashMap<>();

    public HolographicHologramManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public void createLeaderboardHologram(Location loc, LeaderType type, String formatKey) {
        Hologram hologram = HologramsAPI.createHologram(plugin, loc);

        holograms.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(formatKey, k -> new ArrayList<>())
                .add(hologram);

        saveHologramLocation(loc, type, formatKey);
        updateLeaderboardHolograms(type);
    }

    @Override
    public void updateLeaderboardHolograms(LeaderType type) {
        LeaderboardManager lbManager = plugin.getLeaderboardManager();
        if (lbManager == null || !plugin.serverLoaded() || !SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
            return;
        }

        Map<String, List<Hologram>> typeHolograms = holograms.get(type);
        if (typeHolograms == null) {
            return;
        }

        // Iterate through all registered holograms of the given type
        for (Map.Entry<String, List<Hologram>> entry : typeHolograms.entrySet()) {
            String key = entry.getKey();
            List<String> format = config.getStringList("leaderboard." + type.toString().toLowerCase() + "." + key + ".format");

            if (format.isEmpty()) {
                continue;
            }

            for (Hologram hologram : entry.getValue()) {
                hologram.clearLines();
                for (int i = 0; i < format.size(); i++) {
                    String line = getFormattedString(format.get(i), type);
                    // Handle item lines (e.g., player heads)
                    if (line.startsWith("item:")) {
                        ItemStack item = createItemFromFormat(line, type, lbManager);
                        if (item != null) {
                            hologram.insertItemLine(i, item);
                        } else {
                            hologram.insertTextLine(i, " "); // Insert a blank line if item creation fails
                        }
                    } else {
                        // Handle standard text lines
                        hologram.insertTextLine(i, ChatColor.translateAlternateColorCodes('&', line));
                    }
                }
            }
        }
    }

    @Override
    public boolean removeHologram(Location loc) {
        Hologram hologramToRemove = null;
        LeaderType typeToRemove = null;
        String keyToRemove = null;
        double minDistanceSquared = Double.MAX_VALUE;

        // Find the closest hologram to the specified location
        for (Map.Entry<LeaderType, Map<String, List<Hologram>>> typeEntry : holograms.entrySet()) {
            if (!SkyWarsReloaded.getCfg().isTypeEnabled(typeEntry.getKey())) continue;

            for (Map.Entry<String, List<Hologram>> keyEntry : typeEntry.getValue().entrySet()) {
                for (Hologram hologram : keyEntry.getValue()) {
                    if (hologram.isDeleted()) continue;

                    double distanceSquared = loc.distanceSquared(hologram.getLocation());
                    if (distanceSquared < minDistanceSquared) {
                        minDistanceSquared = distanceSquared;
                        hologramToRemove = hologram;
                        typeToRemove = typeEntry.getKey();
                        keyToRemove = keyEntry.getKey();
                    }
                }
            }
        }

        // Remove the hologram if it's within a 2-block radius to prevent accidental removals
        if (hologramToRemove == null || minDistanceSquared >= 4) {
            return false;
        }

        hologramToRemove.delete();
        holograms.get(typeToRemove).get(keyToRemove).remove(hologramToRemove);
        removeHologramLocation(hologramToRemove.getLocation(), typeToRemove, keyToRemove);
        return true;
    }
}
