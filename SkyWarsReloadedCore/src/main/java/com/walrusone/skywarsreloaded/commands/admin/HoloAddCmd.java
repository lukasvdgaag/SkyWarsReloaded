package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class HoloAddCmd extends BaseCmd {

    public HoloAddCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "hologram";
        alias = new String[]{"h"};
        argLength = 3;
    }

    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {
        if (plugin.getHologramManager() == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.holograms-not-enabled"));
            return true;
        }

        LeaderType type = LeaderType.matchType(args[1].toUpperCase());
        if (type == null || !SkyWarsReloaded.get().getLeaderTypes().contains(type.toString())) {
            StringJoiner types = new StringJoiner(", ");
            for (String add : SkyWarsReloaded.get().getLeaderTypes()) {
                types.add(add);
            }
            player.sendMessage(new Messaging.MessageFormatter()
                    .setVariable("validtypes", types.toString())
                    .format("leaderboard.invalidtype")
            );
            return true;
        }
        String format = args[2];
        if (plugin.getHologramManager().getFormats(type).contains(format)) {
            if (plugin.getHologramManager().isHologramAtLocation(player.getLocation())) {
                player.sendMessage(new Messaging.MessageFormatter().format("command.hologram-already-exists"));
                return true;
            }

            plugin.getHologramManager().createLeaderboardHologram(player.getEyeLocation(), type, format);
            player.sendMessage(new Messaging.MessageFormatter()
                    .setVariable("type", type.name())
                    .setVariable("format", format)
                    .format("command.hologram-created")
            );
            return true;
        }

        StringJoiner formats = new StringJoiner(", ");
        for (String add : plugin.getHologramManager().getFormats(type)) {
            formats.add(add);
        }
        player.sendMessage(new Messaging.MessageFormatter()
                .setVariable("validtypes", formats.toString())
                .format("leaderboard.invalidformat")
        );
        return true;
    }
}