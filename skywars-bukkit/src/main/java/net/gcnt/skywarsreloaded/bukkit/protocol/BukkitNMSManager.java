package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.protocol.NMS;
import net.gcnt.skywarsreloaded.protocol.NMSManager;
import org.bukkit.Bukkit;

public class BukkitNMSManager implements NMSManager {

    @SuppressWarnings("FieldCanBeLocal")
    private final SkyWarsReloaded plugin;
    private final NMS nms;

    public BukkitNMSManager(SkyWarsReloaded pluginIn) {
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
