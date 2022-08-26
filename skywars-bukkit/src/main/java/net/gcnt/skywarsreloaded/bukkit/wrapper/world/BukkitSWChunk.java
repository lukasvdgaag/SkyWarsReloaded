package net.gcnt.skywarsreloaded.bukkit.wrapper.world;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.world.SWChunk;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Chunk;

public class BukkitSWChunk implements SWChunk {

    private final BukkitSkyWarsReloaded plugin;
    private final Chunk chunk;

    public BukkitSWChunk(BukkitSkyWarsReloaded plugin, Chunk chunk) {
        this.plugin = plugin;
        this.chunk = chunk;
    }

    @Override
    public boolean isLoaded() {
        return chunk.isLoaded();
    }

    @Override
    public void load() {
        chunk.load();
    }

    @Override
    public void unload() {
        chunk.unload();
    }

    @Override
    public SWWorld getWorld() {
        return plugin.getServer().getWorld(chunk.getWorld().getName());
    }
}
