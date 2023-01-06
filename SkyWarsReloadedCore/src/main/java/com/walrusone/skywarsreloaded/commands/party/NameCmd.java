package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NameCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public NameCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "name";
        alias = new String[]{"n"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        Party party = Party.getParty(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.notinaparty"));
            return true;
        }

        if (!party.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.mustbepartyleader"));
            return true;
        }

        String partyName = args[1];
        party.setPartyName(partyName);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", partyName).format("party.rename"));
        return true;
    }
}
