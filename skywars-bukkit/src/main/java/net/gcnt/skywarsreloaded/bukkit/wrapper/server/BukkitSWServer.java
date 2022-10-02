package net.gcnt.skywarsreloaded.bukkit.wrapper.server;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.AbstractSWServer;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.OfflinePlayer;
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

    public Server getBukkitServer() {
        return bukkitServer;
    }

    public OfflinePlayer getOfflinePlayer(SWPlayer player) {
        return bukkitServer.getOfflinePlayer(player.getUuid());
    }

    @Override
    public SWWorld getDefaultWorld() {
        return this.getWorld(defaultWorld);
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return bukkitServer.getPluginManager().isPluginEnabled(pluginName);
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

    @Override
    public void registerWorld(UUID serverWorldUUID, SWWorld world) {
        this.worldCache.put(serverWorldUUID, world);
    }

    public SWWorld getWorld(World bukkitWorld) {
        // Get world from cache
        UUID uuid = bukkitWorld.getUID();
        SWWorld swWorld = this.worldCache.get(uuid);

        // Create wrapper and cache if not yet present
        if (swWorld == null) {
            swWorld = new BukkitSWWorld(this.plugin, bukkitWorld);
            this.registerWorld(uuid, swWorld);
        }

        return swWorld;
    }

    @Override
    public SWInventory createInventory(String title, int size) {
        return new BukkitSWInventory(plugin, title, size);
    }
}
