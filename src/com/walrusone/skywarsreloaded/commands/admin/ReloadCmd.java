package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;

public class ReloadCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd
{
  public ReloadCmd(String t)
  {
    type = t;
    forcePlayer = false;
    cmdName = "reload";
    alias = new String[] { "r" };
    argLength = 1;
  }
  
  public boolean run()
  {
    SkyWarsReloaded.get().onDisable();
    SkyWarsReloaded.get().load();
    sender.sendMessage(new Messaging.MessageFormatter().format("command.reload"));
    return true;
  }
}
