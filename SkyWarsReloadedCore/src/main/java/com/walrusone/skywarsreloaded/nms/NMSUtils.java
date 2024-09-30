package com.walrusone.skywarsreloaded.nms;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMSUtils {

    public static NMS loadNMS(SkyWarsReloaded plugin) {
        String packageName = plugin.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        CompatibleNMSVersion selectedNMSVersion = null;
        for (CompatibleNMSVersion key : CompatibleNMSVersion.values()) {
            if (key.name().equals(version)) {
                selectedNMSVersion = key;
                break;
            }
        }

        plugin.getLogger().info("Trying to load NMS support for server version '" + version + " (" + packageName + ")'...");

        if (selectedNMSVersion == null) {
            Integer currentFeatureVersion = null;
            try {
                currentFeatureVersion = Integer.parseInt(version.split("_")[1]);
            } catch (Exception e) {
                String regex = "(\\d+)\\.(\\d+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(version);
                if (matcher.find()) {
                    // Extract and return the minor version (which is considered the "major version")
                    currentFeatureVersion = Integer.parseInt(matcher.group(2));
                }
            }

            selectedNMSVersion = CompatibleNMSVersion.getLatestSupported(currentFeatureVersion);
            int latestSupportedFeatureVersion = selectedNMSVersion.getFeatureVersion();

            if (currentFeatureVersion != null && currentFeatureVersion >= latestSupportedFeatureVersion) {
                plugin.getLogger().warning(
                        "===================\n" +
                                "It seems like we have not validated this newer version of Spigot/Bukkit (" + version + ").\n" +
                                "We will try to load the plugin with the latest supported version: " + selectedNMSVersion.name() + ", with handler: " + selectedNMSVersion.getNmsImplVersion() + ".\n" +
                                "Some features may not work as intended. However, this should be relatively stable on 1.19+\n" +
                                "If you experience any issues, please join our Discord server (https://www.gcnt.net/discord) and wait for an official release with proper support.\n" +
                                "===================");
            }
            /*else {
                plugin.getLogger().severe(
                        "===================\n" +
                                "It seems like we do not support this version of Spigot/Bukkit (v" + version + ").\n" +
                                "You are most likely using an outdated or deprecated version of Spigot/Bukkit.\n" +
                                "If you feel like this is a mistake, please join our Discord server (https://www.gcnt.net/discord).\n" +
                                "===================");
                return null;
            }*/
        }

        try {
            String nmsImplVersionStr = selectedNMSVersion.getNmsImplVersion();
            final Class<?> clazz = Class.forName("com.walrusone.skywarsreloaded.nms." + nmsImplVersionStr + ".NMSHandler");
            if (NMS.class.isAssignableFrom(clazz)) {
                plugin.getLogger().info("Loaded support for NMS server version " + version + " using handler: " + nmsImplVersionStr + ".");
                return (NMS) clazz.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        plugin.getLogger().severe(
                "===================\n" +
                        "It seems like we do not support this version of Spigot/Bukkit (v" + version + ").\n" +
                        "If you feel like this is a mistake, please join our Discord server (https://www.gcnt.net/discord).\n" +
                        "===================");
        return null;
    }

}
