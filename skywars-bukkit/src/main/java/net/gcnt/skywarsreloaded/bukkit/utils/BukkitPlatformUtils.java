package net.gcnt.skywarsreloaded.bukkit.utils;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.World;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.AbstractPlatformUtils;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BukkitPlatformUtils extends AbstractPlatformUtils {

    private final SkyWarsReloaded plugin;

    public BukkitPlatformUtils(SkyWarsReloaded plugin) {
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
