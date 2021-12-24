package net.gcnt.skywarsreloaded.bukkit.wrapper.server;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.wrapper.server.AbstractSWServer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Server;
import org.bukkit.World;

import java.util.HashMap;
import java.util.UUID;

public class BukkitSWServer extends AbstractSWServer {

    private final BukkitSkyWarsReloaded plugin;
    private final Server bukkitServer;
    private final World defaultWorld;
    private final HashMap<UUID, SWWorld> worldCache;

    public BukkitSWServer(BukkitSkyWarsReloaded pluginIn) {
        super(pluginIn);
        this.plugin = pluginIn;
        this.bukkitServer = pluginIn.getBukkitPlugin().getServer();
        this.defaultWorld = this.bukkitServer.getWorlds().get(0);
        this.worldCache = new HashMap<>();
    }

    @Override
    public SWWorld getDefaultWorld() {
        return this.getWorld(defaultWorld);
    }

    @Override
    public SWWorld getWorld(String name) {
        // Check underlying world exists
        World bukkitWorld = this.bukkitServer.getWorld(name);
        if (bukkitWorld == null) {
            return null;
        }

        return this.getWorld(bukkitWorld);
    }

    public SWWorld getWorld(World bukkitWorld) {
        // Get world from cache
        UUID uuid = bukkitWorld.getUID();
        SWWorld swWorld = this.worldCache.get(uuid);

        // Create wrapper and cache if not yet present
        if (swWorld == null) {
            swWorld = new BukkitSWWorld(this.plugin, bukkitWorld);
            this.worldCache.put(uuid, swWorld);
        }

        return swWorld;
    }
}
