package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWChunk;
import net.gcnt.skywarsreloaded.wrapper.world.SWChunk;

public class BukkitNMS_14_15 extends BukkitNMS_13 {

    public BukkitNMS_14_15(BukkitSkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    public void addPluginChunkTicket(SWChunk chunk) {
        ((BukkitSWChunk) chunk).getChunk().addPluginChunkTicket(plugin.getBukkitPlugin());
    }
}
