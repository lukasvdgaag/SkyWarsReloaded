package net.gcnt.skywarsreloaded.bukkit.utils;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.World;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.AbstractPlatformUtils;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.TileEntityChest;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class BukkitPlatformUtils extends AbstractPlatformUtils {

    private final BukkitSkyWarsReloaded plugin;

    public BukkitPlatformUtils(BukkitSkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }

    @Override
    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }

    @Override
    public int getBuildLimit() {
        return Bukkit.getServer().getWorlds().get(0).getMaxHeight();
    }

    @Override
    public World getWorldEditWorld(String worldName) {
        org.bukkit.World world = Bukkit.getWorld(worldName);
        if (world == null) return null;
        return new BukkitWorld(world);
    }

    @Override
    public SWWorld getSWWorld(String worldName) {
        return new BukkitSWWorld(plugin, Bukkit.getWorld(worldName));
    }
}
