package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;

public class NameCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd
{
  public NameCmd(String t)
  {
    type = t;
    forcePlayer = false;
    cmdName = "name";
    alias = new String[] { "n" };
    argLength = 3;
  }
  
  public boolean run()
  {
    String worldName = args[1];
    StringBuilder displayName = new StringBuilder();
    for (int i = 2; i < args.length; i++) {
      displayName.append(args[i]);
      displayName.append(" ");
    }
    displayName.substring(0, displayName.length() - 1);
    if (displayName.length() == 0) {
      sender.sendMessage(new Messaging.MessageFormatter().format("error.map-name"));
      return false;
    }
    
    GameMap map = GameMap.getMap(worldName);
    if (map != null) {
      map.setDisplayName(displayName.toString());
      sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).setVariable("displayname", args[2]).format("maps.name"));
      
      return true;
    }
    sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
    return true;
  }
}
