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

public abstract class AbstractHologramsManager implements HologramManager {

    protected final SkyWarsReloaded plugin;
    protected FileConfiguration config;
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
}