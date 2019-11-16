package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;

public class SWJoinCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd
{
  public SWJoinCmd(String t)
  {
    type = t;
    forcePlayer = true;
    cmdName = "join";
    alias = new String[] { "j" };
    argLength = 1;
  }
  
  public boolean run()
  {
    GameType type = GameType.ALL;
    if (args.length > 1) {
      if ((args[1].equalsIgnoreCase("single")) || (args[1].equalsIgnoreCase("solo"))) {
        type = GameType.SINGLE;
      } else if (args[1].equalsIgnoreCase("team")) {
        type = GameType.TEAM;
      }
    }
    boolean joined = MatchManager.get().joinGame(player, type);
    int count = 0;
    while ((count < 4) && (!joined)) {
      joined = MatchManager.get().joinGame(player, type);
      count++;
    }
    if (!joined) {
      player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join"));
    }
    return true;
  }
}
