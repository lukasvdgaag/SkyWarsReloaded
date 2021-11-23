package net.gcnt.skywarsreloaded.bukkit.wrapper.world;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.AbstractSWWorld;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWWorld extends AbstractSWWorld {

    private final SkyWarsReloaded plugin;
    private final World bukkitWorld;

    public BukkitSWWorld(SkyWarsReloaded pluginIn, World worldIn) {
        this.plugin = pluginIn;
        this.bukkitWorld = worldIn;
    }

    @Override
    public String getName() {
        return bukkitWorld.getName();
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
        if (item instanceof BukkitItem bukkitItem) {
            block.setType(bukkitItem.getBukkitItem().getType());
        } else {
            try {
                block.setType(Material.valueOf(item.getMaterial().toUpperCase()));
            } catch (Exception ignored) {
            }
        }
    }
}
