package net.gcnt.skywarsreloaded.bukkit.utils;

import net.gcnt.skywarsreloaded.utils.AbstractUtilities;
import net.gcnt.skywarsreloaded.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BukkitUtils extends AbstractUtilities {

    @Override
    public String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }

    @Override
    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }
}
