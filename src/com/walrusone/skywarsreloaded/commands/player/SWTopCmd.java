package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.managers.Leaderboard;
import com.walrusone.skywarsreloaded.managers.Leaderboard.LeaderData;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;
import java.util.List;
import org.bukkit.entity.Player;

public class SWTopCmd extends BaseCmd
{
  public SWTopCmd(String t)
  {
    type = t;
    forcePlayer = true;
    cmdName = "top";
    alias = new String[] { "leaderboard" };
    argLength = 2;
  }
  
  public boolean run() {
    int i;
    if (SkyWarsReloaded.get().getUseable().contains(args[1].toUpperCase())) {
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
        Leaderboard.LeaderData playerData = (Leaderboard.LeaderData)top.get(i);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("rank", "" + (i + 1))
          .setVariable("player", playerData.getName())
          .setVariable("elo", "" + playerData.getElo())
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
    StringBuilder types = new StringBuilder();
    for (String add : SkyWarsReloaded.get().getUseable()) {
      types.append(add);
      types.append(", ");
    }
    types.substring(0, types.length() - 2);
    player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
    return false;
  }
}
