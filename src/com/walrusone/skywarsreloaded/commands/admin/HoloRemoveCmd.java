package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.ChatColor;

public class HoloRemoveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public HoloRemoveCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "holoremove";
        alias = new String[]{"hr"};
        argLength = 1;
    }

    public boolean run() {
        if (SkyWarsReloaded.getCfg().hologramsEnabled()) {
            boolean result = SkyWarsReloaded.getHoloManager().removeHologram(player.getLocation());
            if (result) {
                player.sendMessage(ChatColor.GREEN + "The closest hologram was removed");
                return true;
            }
            player.sendMessage(ChatColor.RED + "No holograms were found");
            return false;
        }
        player.sendMessage(ChatColor.RED + "Holograms are not enabled!");
        return false;
    }
}
