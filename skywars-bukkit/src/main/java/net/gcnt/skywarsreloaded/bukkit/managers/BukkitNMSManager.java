package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.protocol.BukkitNMS_12;
import net.gcnt.skywarsreloaded.bukkit.protocol.BukkitNMS_13_15;
import net.gcnt.skywarsreloaded.bukkit.protocol.BukkitNMS_16_18;
import net.gcnt.skywarsreloaded.bukkit.protocol.BukkitNMS_8_11;
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
            this.nms = new BukkitNMS_16_18(this.plugin, serverPackage);
        } else if (version >= 13) {
            this.nms = new BukkitNMS_13_15(this.plugin, serverPackage);
        } else if (version >= 12) {
            this.nms = new BukkitNMS_12(this.plugin, serverPackage);
        } else if (version >= 8) {
            this.nms = new BukkitNMS_8_11(this.plugin, serverPackage);
        } else {
            throw new IllegalStateException("Unsupported server version: " + version);
        }
    }

    public NMS getNMS() {
        return this.nms;
    }
}
