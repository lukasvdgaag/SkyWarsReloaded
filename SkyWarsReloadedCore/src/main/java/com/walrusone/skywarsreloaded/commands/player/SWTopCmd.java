package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.managers.LeaderboardManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.StringJoiner;

public class SWTopCmd extends BaseCmd {
    public SWTopCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        this.type = t;
        this.forcePlayer = true;
        this.cmdName = "top";
        this.alias = new String[] {"leaderboard"};
        this.argLength = 1;
        this.maxArgs = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String leaderType;
        if (args.length < 2) {
            leaderType = "wins";
        } else {
            leaderType = args[1].toUpperCase();
        }

        if (SkyWarsReloaded.get().getLeaderTypes().contains(leaderType)) {
            if (!SkyWarsReloaded.get().getLeaderboardManager().loaded(LeaderType.valueOf(args[1].toUpperCase()))) {
                player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.updating"));
                return true;
            }

            List<LeaderboardManager.LeaderData> top = SkyWarsReloaded.get().getLeaderboardManager().getTopList(LeaderType.valueOf(args[1].toUpperCase()));

            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.header"));
            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.header2"));
            if (top.isEmpty()) {
                player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.no-data"));
            }

            for (int i = 0; i < top.size(); i++) {
                LeaderboardManager.LeaderData playerData = top.get(i);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("rank", "" + (i + 1))
                        .setVariable("player", playerData.getName())
                        .setVariable("wins", "" + playerData.getWins())
                        .setVariable("losses", "" + playerData.getLoses())
                        .setVariable("kills", "" + playerData.getKills())
                        .setVariable("deaths", "" + playerData.getDeaths())
                        .setVariable("xp", "" + playerData.getXp())
                        .format("leaderboard.player-data"));
            }

            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.footer"));
            return true;
        }

        StringJoiner types = new StringJoiner(", ");
        for (String add : SkyWarsReloaded.get().getLeaderTypes()) {
            types.add(add);
        }

        player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
        return true;
    }
}
