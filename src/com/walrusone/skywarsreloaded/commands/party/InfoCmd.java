package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;
import com.walrusone.skywarsreloaded.utilities.Party;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InfoCmd extends BaseCmd
{
  public InfoCmd(String t)
  {
    type = t;
    forcePlayer = true;
    cmdName = "info";
    alias = new String[] { "in" };
    argLength = 1;
  }
  
  public boolean run()
  {
    Party party = Party.getParty(player);
    if (party == null) {
      player.sendMessage(new Messaging.MessageFormatter().format("party.notinaparty"));
      return false;
    }
    
    player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.info1"));
    player.sendMessage(new Messaging.MessageFormatter().setVariable("leader", Bukkit.getPlayer(party.getLeader()).getName()).format("party.info2"));
    StringBuilder members = new StringBuilder();
    for (UUID uuid : party.getMembers()) {
      members.append(Bukkit.getPlayer(uuid).getName());
      members.append(", ");
    }
    members.substring(0, members.length() - 2);
    player.sendMessage(new Messaging.MessageFormatter().setVariable("members", members.toString()).format("party.info3"));
    return true;
  }
}
