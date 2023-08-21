package com.walrusone.skywarsreloaded.utilities.holograms;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRPlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class HologramsUtil {
    protected static FileConfiguration fc;
    static File holoFile;
    private static HashMap<LeaderType, List<String>> formats = new HashMap<>();

    public abstract void createLeaderHologram(Location loc, LeaderType type, String formatKey);

    public abstract void updateLeaderHolograms(LeaderType type);

    public abstract boolean removeHologram(Location loc);

    void getFC() {
        holoFile = new File(SkyWarsReloaded.get().getDataFolder(), "holograms.yml");

        if (!holoFile.exists()) {
            SkyWarsReloaded.get().saveResource("holograms.yml", false);
        }

        if (holoFile.exists()) {
            fc = YamlConfiguration.loadConfiguration(holoFile);
            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#debug::getFC: fc = " + fc);
            }
            for (LeaderType type : LeaderType.values()) {
                if (fc.getConfigurationSection("leaderboard." + type.toString().toLowerCase()) != null) {
                    for (String key : fc.getConfigurationSection("leaderboard." + type.toString().toLowerCase()).getKeys(false)) {
                        formats.computeIfAbsent(type, k -> new ArrayList<>());
                        formats.get(type).add(key);
                        if (SkyWarsReloaded.getCfg().debugEnabled()) {
                            SkyWarsReloaded.get().getLogger().info(this.getClass().getName() + "#debug::getFC: key = " + key);
                        }
                    }
                }
            }
        }
    }

    public void load() {
        if (fc == null) {
            getFC();
        }
        if (fc != null) {
            for (LeaderType type : LeaderType.values()) {
                if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                    if (fc.getConfigurationSection("leaderboard." + type.toString().toLowerCase()) != null) {
                        for (String key : fc.getConfigurationSection("leaderboard." + type.toString().toLowerCase()).getKeys(false)) {
                            List<String> holograms = fc.getStringList("leaderboard." + type.toString().toLowerCase() + "." + key + ".locations");
                            if (holograms != null) {
                                for (String hologram : holograms) {
                                    createLeaderHologram(Util.get().stringToLocation(hologram), type, key);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    String getFormattedString(String string, @Nullable LeaderType type) {
        String toReturn = string;
        if (string.startsWith("item:")) {
            return string;
        }
        String[] variables = StringUtils.substringsBetween(string, "{", "}");
        if (variables == null) {
            return string;
        }
        for (String var : variables) {
            String value = SWRPlaceholderAPI.getLeaderBoardVariable(var, type);
            toReturn = toReturn.replaceAll("\\{" + var + "}", value);
        }
        return toReturn;
    }



    public List<String> getFormats(LeaderType type) {
        return formats.get(type);
    }

}