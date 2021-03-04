package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.managers.Leaderboard;
import com.walrusone.skywarsreloaded.utilities.Messaging;

import java.util.List;
import java.util.StringJoiner;

public class SWTopCmd extends BaseCmd {
    public SWTopCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "top";
        alias = new String[]{"leaderboard"};
        argLength = 2;
    }

    public boolean run() {
        int i;
        if (SkyWarsReloaded.get().getUsable().contains(args[1].toUpperCase())) {
            if (!SkyWarsReloaded.getLB().loaded(LeaderType.valueOf(args[1].toUpperCase()))) {
                player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.updating"));
                return true;
            }
            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.header"));
            List<Leaderboard.LeaderData> top = SkyWarsReloaded.getLB().getTopList(LeaderType.valueOf(args[1].toUpperCase()));
            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.header2"));
            if (top.size() == 0) {
                player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.no-data"));
            }
            for (i = 0; i < top.size(); i++) {
                Leaderboard.LeaderData playerData = top.get(i);
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
        for (String add : SkyWarsReloaded.get().getUsable()) {
            types.add(add);
        }
        player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
        return true;
    }
}
