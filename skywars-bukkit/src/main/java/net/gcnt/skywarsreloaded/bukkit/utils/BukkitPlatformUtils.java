package net.gcnt.skywarsreloaded.bukkit.utils;

import net.gcnt.skywarsreloaded.utils.AbstractPlatformUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BukkitPlatformUtils extends AbstractPlatformUtils {

    @Override
    public String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }

    @Override
    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }
}
