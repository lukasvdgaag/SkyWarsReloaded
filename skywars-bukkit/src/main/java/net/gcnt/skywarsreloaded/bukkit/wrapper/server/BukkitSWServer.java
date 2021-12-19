package net.gcnt.skywarsreloaded.bukkit.wrapper.server;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.server.AbstractSWServer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.World;

public class BukkitSWServer extends AbstractSWServer {

    private final World defaultWorld;

    public BukkitSWServer(BukkitSkyWarsReloaded pluginIn) {
        super(pluginIn);
        this.defaultWorld = pluginIn.getPlugin().getServer().getWorlds().get(0);
    }

    @Override
    public SWWorld getDefaultWorld() {
        return null;
    }

    @Override
    public SWWorld getWorld(String name) {
        return null;
    }
}
