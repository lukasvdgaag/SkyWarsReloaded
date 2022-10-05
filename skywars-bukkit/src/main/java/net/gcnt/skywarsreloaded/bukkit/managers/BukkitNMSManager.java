package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.protocol.*;
import net.gcnt.skywarsreloaded.manager.NMSManager;
import net.gcnt.skywarsreloaded.protocol.NMS;
import org.bukkit.Bukkit;

public class BukkitNMSManager implements NMSManager {

    @SuppressWarnings("FieldCanBeLocal")
    private final BukkitSkyWarsReloaded plugin;
    private final NMS nms;

    public BukkitNMSManager(BukkitSkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;

        int version = plugin.getUtils().getServerVersion();
        String serverPackage = Bukkit.getServer().getClass().getPackage().getName();

        if (version >= 16) {
            this.nms = new BukkitNMS_16_19(this.plugin, serverPackage);
        } else if (version >= 14) {
            this.nms = new BukkitNMS_14_15(this.plugin, serverPackage);
        } else if (version >= 13) {
            this.nms = new BukkitNMS_13(this.plugin, serverPackage);
        } else if (version >= 12) {
            this.nms = new BukkitNMS_12(this.plugin, serverPackage);
        } else if (version >= 9) {
            this.nms = new BukkitNMS_9_11(this.plugin, serverPackage);
        } else if (version >= 8) {
            this.nms = new BukkitNMS_8(this.plugin, serverPackage);
        } else {
            throw new IllegalStateException("Unsupported server version: " + version);
        }
    }

    public NMS getNMS() {
        return this.nms;
    }
}
