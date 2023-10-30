package com.walrusone.skywarsreloaded.utilities;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.nms.NMS;

import java.util.LinkedHashMap;
import java.util.List;

public class NMSUtils {

    private final static LinkedHashMap<String, List<String>> compatibleNMSVersions;

    static {
        compatibleNMSVersions = new LinkedHashMap<>();
        compatibleNMSVersions.put("1_8_R3", Lists.newArrayList("1_8_R3"));
        compatibleNMSVersions.put("1_9_R1", Lists.newArrayList("1_9_R1"));
        compatibleNMSVersions.put("1_9_R2", Lists.newArrayList("1_9_R2", "1_9_R3"));
        compatibleNMSVersions.put("1_10_R1", Lists.newArrayList("1_10_R1"));
        compatibleNMSVersions.put("1_11_R1", Lists.newArrayList("1_11_R1"));
        compatibleNMSVersions.put("1_12_R1", Lists.newArrayList("1_12_R1"));
        compatibleNMSVersions.put("1_13_R2", Lists.newArrayList("1_13_R2")); // no 1.13.1 supported.
        compatibleNMSVersions.put("1_14_R1", Lists.newArrayList("1_14_R1"));
        compatibleNMSVersions.put("1_15_R1", Lists.newArrayList("1_15_R1"));
        compatibleNMSVersions.put("1_16_R1", Lists.newArrayList("1_16_R1"));
        compatibleNMSVersions.put("1_16_R2", Lists.newArrayList("1_16_R2"));
        compatibleNMSVersions.put("1_16_R3", Lists.newArrayList("1_16_R3"));
        compatibleNMSVersions.put("1_17_R1", Lists.newArrayList("1_17_R1"));
        compatibleNMSVersions.put("1_18_R2", Lists.newArrayList("1_18_R2", "1_18_R3"));
        compatibleNMSVersions.put("1_19_R1", Lists.newArrayList("1_19_R1", "1_19_R2", "1_19_R3", "1_20_R1", "1_20_R2"));
    }

    public static NMS loadNMS(SkyWarsReloaded plugin) {
        String packageName = plugin.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1).replace("v", "");

        String selected = null;
        for (String key : compatibleNMSVersions.keySet()) {
            if (compatibleNMSVersions.get(key).contains(version)) {
                selected = key;
                break;
            }
        }

        int currentNumeric = Integer.parseInt(version.split("_")[1]);
        final String[] versionArray = compatibleNMSVersions.keySet().toArray(new String[]{});
        final String latestVersion = versionArray[versionArray.length - 1];
        int latestNumeric = Integer.parseInt(latestVersion.split("_")[1]);

        if (selected == null) {
            if (currentNumeric >= latestNumeric) {
                selected = latestVersion;
                plugin.getLogger().warning("===================\n" +
                        "It seems like we do not yet fully support this newer version of Spigot/Bukkit (v" + version + ").\n" +
                        "We will try to load the plugin up with the latest supported version (v" + latestVersion + ").\n" +
                        "Please know that this is untested and that some features may not work as intended.\n" +
                        "If you experience any issues, please join our Discord server (https://www.gcnt.net/discord) and wait for an official release with proper support.\n" +
                        "===================");
            } else {
                plugin.getLogger().severe("===================\n" +
                        "It seems like we do not support this version of Spigot/Bukkit (v" + version + ").\n" +
                        "You are most likely using an outdated or deprecated version of Spigot/Bukkit.\n" +
                        "If you feel like this is a mistake, please join our Discord server (https://www.gcnt.net/discord).\n" +
                        "===================");
                return null;
            }
        }

        try {
            final Class<?> clazz = Class.forName("net.gcnt.additionsplus.NMS.NMS_" + selected);
            if (NMS.class.isAssignableFrom(clazz)) {
                plugin.getLogger().info("Loaded support for NMS server version " + version + " using the v" + selected + " loader.");
                return (NMS) clazz.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        plugin.getLogger().severe("===================\n" +
                "It seems like we do not support this version of Spigot/Bukkit (v" + version + ").\n" +
                "If you feel like this is a mistake, please join our Discord server (https://www.gcnt.net/discord).\n" +
                "===================");
        return null;
    }

}
