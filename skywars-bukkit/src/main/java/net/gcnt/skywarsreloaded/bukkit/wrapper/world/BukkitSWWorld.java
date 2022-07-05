package net.gcnt.skywarsreloaded.bukkit.wrapper.world;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.AbstractSWWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWWorld extends AbstractSWWorld {

    private final BukkitSkyWarsReloaded plugin;
    private final World bukkitWorld;
    private final String name;

    public BukkitSWWorld(BukkitSkyWarsReloaded pluginIn, World worldIn) {
        this.plugin = pluginIn;
        this.bukkitWorld = worldIn;
        this.name = worldIn.getName();
    }

    @Override
    public List<SWPlayer> getAllPlayers() {
        return this.bukkitWorld.getPlayers().stream().map(
                (bPlayer) -> this.plugin.getPlayerManager().getPlayerByUUID(bPlayer.getUniqueId())
        ).collect(Collectors.toList());
    }

    @Override
    public void setBlockAt(SWCoord location, Item item) {
        Block block = bukkitWorld.getBlockAt(location.x(), location.y(), location.z());
        if (item == null) block.setType(Material.AIR);
        else if (item instanceof BukkitItem) {
            block.setType(((BukkitItem) item).getBukkitItem().getType());
        } else {
            try {
                block.setType(Material.valueOf(item.getMaterial().toUpperCase()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setBlockAt(SWCoord location, String blockName) {
        this.setBlockAt(location, blockName == null ? null : new BukkitItem(this.plugin, blockName));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SWCoord getDefaultSpawnLocation() {
        Location loc = this.bukkitWorld.getSpawnLocation();
        return new CoreSWCoord(this, loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void unload(boolean saveChunks) {
        this.plugin.getBukkitPlugin().getServer().unloadWorld(this.bukkitWorld, saveChunks);
    }

    @Override
    public boolean isLoaded() {
        return bukkitWorld != null;
    }

    @Override
    public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
        this.bukkitWorld.setKeepSpawnInMemory(keepSpawnLoaded);
    }

    public World getBukkitWorld() {
        return bukkitWorld;
    }
}
