package net.gcnt.skywarsreloaded.bukkit.wrapper.world.block;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.world.SWChunk;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWBlock;
import org.bukkit.block.Block;

public class BukkitSWBlock implements SWBlock {

    protected final BukkitSkyWarsReloaded plugin;
    private Block block;

    public BukkitSWBlock(BukkitSkyWarsReloaded plugin, Block block) {
        this.plugin = plugin;
        this.block = block;
    }

    @Override
    public SWChunk getChunk() {
        return null;
    }

    @Override
    public SWCoord getCoord() {
        return new CoreSWCoord(plugin.getServer().getWorld(block.getWorld().getName()), block.getX(), block.getY(), block.getZ());
    }

    @Override
    public String getMaterial() {
        return this.block.getType().name();
    }
}
