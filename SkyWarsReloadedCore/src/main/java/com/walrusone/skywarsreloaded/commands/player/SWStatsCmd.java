package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWStatsCmd extends BaseCmd {
    public SWStatsCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "stats";
        alias = new String[]{"s"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        Player statPlayer = player;
        if (args.length > 1) {
            for (Player playerMatch : Bukkit.getOnlinePlayers()) {
                if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
                    statPlayer = playerMatch;
                }
            }
        }

        PlayerStat playerData = PlayerStat.getPlayerStats(statPlayer);
        if ((playerData != null) && (playerData.isInitialized())) {
            player.sendMessage(new Messaging.MessageFormatter().format("stats.header"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("player", playerData.getPlayerName()).format("stats.name"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("wins", "" + playerData.getWins())
                    .setVariable("losses", "" + playerData.getLosses()).format("stats.win-loss"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kills", "" + playerData.getKills())
                    .setVariable("deaths", "" + playerData.getDeaths()).format("stats.kill-death"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("xp", "" + playerData.getXp())
                    .format("stats.xp"));
            player.sendMessage(new Messaging.MessageFormatter().format("stats.header"));
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("stats.not-ready"));
        }
        return true;
    }
}
