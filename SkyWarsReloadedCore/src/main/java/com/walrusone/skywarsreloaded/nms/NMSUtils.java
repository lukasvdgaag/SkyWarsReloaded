package com.walrusone.skywarsreloaded.nms;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;

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

        int currentFeatureVersion = Integer.parseInt(version.split("_")[1]);
        final CompatibleNMSVersion latestVersion = CompatibleNMSVersion.getLatestSupported();
        int latestSupportedFeatureVersion = latestVersion.getFeatureVersion();

        if (selectedNMSVersion == null) {
            if (currentFeatureVersion >= latestSupportedFeatureVersion) {
                selectedNMSVersion = latestVersion;
                plugin.getLogger().warning(
                        "===================\n" +
                        "It seems like we have not validated this newer version of Spigot/Bukkit (" + version + ").\n" +
                        "We will try to load the plugin with the latest supported version: " + latestVersion.name() + ", with handler: " + latestVersion.getNmsImplVersion() + ".\n" +
                        "Some features may not work as intended. However, this should be relatively stable on 1.19+\n" +
                        "If you experience any issues, please join our Discord server (https://www.gcnt.net/discord) and wait for an official release with proper support.\n" +
                        "===================");
            } else {
                plugin.getLogger().severe(
                        "===================\n" +
                        "It seems like we do not support this version of Spigot/Bukkit (v" + version + ").\n" +
                        "You are most likely using an outdated or deprecated version of Spigot/Bukkit.\n" +
                        "If you feel like this is a mistake, please join our Discord server (https://www.gcnt.net/discord).\n" +
                        "===================");
                return null;
            }
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
