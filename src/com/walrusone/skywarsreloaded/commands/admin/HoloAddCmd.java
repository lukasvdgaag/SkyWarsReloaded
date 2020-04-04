package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class HoloAddCmd extends BaseCmd {

    public HoloAddCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "hologram";
        alias = new String[]{"h"};
        argLength = 3; //counting cmdName
    }

    @Override
    public boolean run() {
        if (SkyWarsReloaded.getCfg().hologramsEnabled()) {
            if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                LeaderType type = LeaderType.matchType(args[1].toUpperCase());
                if (type == null || !SkyWarsReloaded.get().getUseable().contains(type.toString())) {
                    StringBuilder types = new StringBuilder();
                    for (String add : SkyWarsReloaded.get().getUseable()) {
                        types.append(add);
                        types.append(", ");
                    }
                    types.substring(0, types.length() - 2);
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
                    return false;
                }
                String format = args[2];
                if (SkyWarsReloaded.getHoloManager().getFormats(type).contains(format)) {
                    SkyWarsReloaded.getHoloManager().createLeaderHologram(player.getEyeLocation(), type, format);
                    return true;
                }

                StringBuilder formats = new StringBuilder();
                for (String add : SkyWarsReloaded.getHoloManager().getFormats(type)) {
                    formats.append(add);
                    formats.append(", ");
                }
                formats.substring(0, formats.length() - 2);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", formats.toString()).format("leaderboard.invalidformat"));
                return false;
            }
            player.sendMessage(ChatColor.RED + "You must have installed and enabled the HolographicDisplays plugin in order to use Holograms");
            return false;
        }
        player.sendMessage(ChatColor.RED + "Holograms are not enabled!");
        return false;
    }

}