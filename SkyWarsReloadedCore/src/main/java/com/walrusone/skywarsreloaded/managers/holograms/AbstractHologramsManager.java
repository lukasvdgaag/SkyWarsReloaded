package com.walrusone.skywarsreloaded.managers.holograms;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.managers.LeaderboardManager;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRPlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractHologramsManager<T> implements HologramManager {

    protected final SkyWarsReloaded plugin;
    protected FileConfiguration config;
    protected final Map<LeaderType, Map<String, List<T>>> holograms = new HashMap<>();

    private final Map<LeaderType, List<String>> formats = new HashMap<>();

    public AbstractHologramsManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    private void loadConfig() {
        File holoFile = new File(plugin.getDataFolder(), "holograms.yml");
        if (!holoFile.exists()) {
            plugin.saveResource("holograms.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(holoFile);

        formats.clear();
        for (LeaderType type : LeaderType.values()) {
            String sectionPath = "leaderboard." + type.toString().toLowerCase();
            if (config.getConfigurationSection(sectionPath) != null) {
                Set<String> keys = config.getConfigurationSection(sectionPath).getKeys(false);
                formats.put(type, new ArrayList<>(keys));
            }
        }
    }

    @Override
    public void load() {
        loadConfig();
        loadHologramsFromConfig();
    }

    private void loadHologramsFromConfig() {
        for (LeaderType type : LeaderType.values()) {
            if (!SkyWarsReloaded.getCfg().isTypeEnabled(type)) continue;

            String sectionPath = "leaderboard." + type.toString().toLowerCase();
            if (config.getConfigurationSection(sectionPath) == null) continue;

            for (String key : config.getConfigurationSection(sectionPath).getKeys(false)) {
                List<String> hologramLocations = config.getStringList(sectionPath + "." + key + ".locations");
                for (String locStr : hologramLocations) {
                    Location loc = Util.get().stringToLocation(locStr);
                    createLeaderboardHologram(loc, type, key);
                }
            }
        }
    }

    @Override
    public List<String> getFormats(LeaderType type) {
        return formats.getOrDefault(type, new ArrayList<>());
    }

    protected String getFormattedString(String string, @Nullable LeaderType type) {
        if (string.startsWith("item:")) {
            return string;
        }
        String[] variables = StringUtils.substringsBetween(string, "{", "}");
        if (variables == null) {
            return string;
        }
        String result = string;
        for (String var : variables) {
            String value = SWRPlaceholderAPI.getLeaderBoardVariable(var, type);
            result = result.replaceAll("\\{" + var + "}", value);
        }
        return result;
    }

    /**
     * Creates an ItemStack based on a format string from the hologram configuration.
     *
     * @param line      The format line, e.g., "item:playerhead{1}".
     * @param type      The leaderboard type.
     * @param lbManager The leaderboard manager instance.
     * @return The created ItemStack, or null if invalid.
     */
    protected ItemStack createItemFromFormat(String line, LeaderType type, LeaderboardManager lbManager) {
        String materialString = line.substring(5);
        if (materialString.toLowerCase().startsWith("playerhead")) {
            String numStr = materialString.substring(11, materialString.length() - 1);
            if (!Util.get().isInteger(numStr)) return null;

            int rank = Integer.parseInt(numStr) - 1;
            List<LeaderboardManager.LeaderData> topList = lbManager.getTopList(type);
            if (topList == null || rank < 0 || rank >= topList.size()) return null;

            // Player must be online to retrieve their skin for the skull
            Player player = Bukkit.getPlayer(topList.get(rank).getUUID());
            if (player == null) return null;

            ItemStack head = (SkyWarsReloaded.getNMS().getVersion() < 13)
                    ? new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3)
                    : new ItemStack(Material.valueOf("PLAYER_HEAD"), 1);

            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                SkyWarsReloaded.getNMS().updateSkull(meta, player);
                meta.setDisplayName(ChatColor.YELLOW + player.getName());
                head.setItemMeta(meta);
            }
            return head;
        } else {
            Material material = Material.matchMaterial(materialString);
            if (material != null) {
                return new ItemStack(material, 1);
            }
        }
        return null;
    }

    /**
     * Saves a hologram's location to the holograms.yml file.
     */
    protected void saveHologramLocation(Location loc, LeaderType type, String formatKey) {
        String path = "leaderboard." + type.toString().toLowerCase() + "." + formatKey + ".locations";
        List<String> locations = config.getStringList(path);
        String locStr = Util.get().locationToString(loc);
        if (!locations.contains(locStr)) {
            locations.add(locStr);
            config.set(path, locations);
            try {
                config.save(new File(plugin.getDataFolder(), "holograms.yml"));
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to save hologram location: " + e.getMessage());
            }
        }
    }

    /**
     * Removes a hologram's location from the holograms.yml file.
     */
    protected void removeHologramLocation(Location loc, LeaderType type, String formatKey) {
        String path = "leaderboard." + type.toString().toLowerCase() + "." + formatKey + ".locations";
        List<String> locations = config.getStringList(path);
        String locStr = Util.get().locationToString(loc);

        if (locations.remove(locStr)) {
            config.set(path, locations);
            try {
                config.save(new File(plugin.getDataFolder(), "holograms.yml"));
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to remove hologram location: " + e.getMessage());
            }
        }
    }

    @Override
    public void createLeaderboardHologram(Location loc, LeaderType type, String formatKey) {
        T hologram = createHologram(loc, type, formatKey);

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

        Map<String, List<T>> typeHolograms = holograms.get(type);
        if (typeHolograms == null) {
            return;
        }

        // Iterate through all registered holograms of the given type
        for (Map.Entry<String, List<T>> entry : typeHolograms.entrySet()) {
            String key = entry.getKey();
            List<String> format = config.getStringList("leaderboard." + type.toString().toLowerCase() + "." + key + ".format");

            if (format.isEmpty()) {
                continue;
            }

            for (T hologram : entry.getValue()) {
                clearHologramLines(hologram);
                for (int i = 0; i < format.size(); i++) {
                    String line = getFormattedString(format.get(i), type);
                    // Handle item lines (e.g., player heads)
                    if (line.startsWith("item:")) {
                        ItemStack item = createItemFromFormat(line, type, lbManager);
                        if (item != null) {
                            insertItemLine(hologram, i, item);
                        } else {
                            // Insert a blank line if item creation fails
                            insertTextLine(hologram, i, " ");
                        }
                    } else {
                        // Handle standard text lines
                        insertTextLine(hologram, i, ChatColor.translateAlternateColorCodes('&', line));
                    }
                }
            }
        }
    }


    @Override
    public boolean removeHologram(Location loc) {
        T hologramToRemove = null;
        LeaderType typeToRemove = null;
        String keyToRemove = null;
        double minDistanceSquared = Double.MAX_VALUE;

        // Find the closest hologram to the specified location
        for (Map.Entry<LeaderType, Map<String, List<T>>> typeEntry : holograms.entrySet()) {
            if (!SkyWarsReloaded.getCfg().isTypeEnabled(typeEntry.getKey())) continue;

            for (Map.Entry<String, List<T>> keyEntry : typeEntry.getValue().entrySet()) {
                for (T hologram : keyEntry.getValue()) {
                    if (isDeleted(hologram)) continue;

                    double distanceSquared = loc.distanceSquared(getHologramLocation(hologram));
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

        deleteHologram(hologramToRemove);
        holograms.get(typeToRemove).get(keyToRemove).remove(hologramToRemove);
        removeHologramLocation(getHologramLocation(hologramToRemove), typeToRemove, keyToRemove);
        return true;
    }

    protected abstract void clearHologramLines(T hologram);

    protected abstract void insertTextLine(T hologram, int index, String text);

    protected abstract void insertItemLine(T hologram, int index, ItemStack item);

    protected abstract boolean isDeleted(T hologram);

    protected abstract void deleteHologram(T hologram);

    protected abstract Location getHologramLocation(T hologram);

    protected abstract T createHologram(Location loc, LeaderType type, String formatKey);
}