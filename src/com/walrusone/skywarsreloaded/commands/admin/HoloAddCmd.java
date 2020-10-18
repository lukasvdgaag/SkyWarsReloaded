package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.StringJoiner;

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
                    StringJoiner types = new StringJoiner(", ");
                    for (String add : SkyWarsReloaded.get().getUseable()) {
                        types.add(add);
                    }
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
                    return false;
                }
                String format = args[2];
                if (SkyWarsReloaded.getHoloManager().getFormats(type).contains(format)) {
                    SkyWarsReloaded.getHoloManager().createLeaderHologram(player.getEyeLocation(), type, format);
                    return true;
                }

                StringJoiner formats = new StringJoiner(", ");
                for (String add : SkyWarsReloaded.getHoloManager().getFormats(type)) {
                    formats.add(add);
                }
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