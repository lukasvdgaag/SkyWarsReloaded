package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public AcceptCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "accept";
        alias = new String[]{"a"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        Party party = Party.getParty(player);
        if (party != null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.alreadyinparty"));
            return true;
        }

        party = Party.getPartyOfInvite(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.noinvite"));
            return true;
        }

        boolean result = party.acceptInvite(player);
        if (result) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.youjoined"));
        } else {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.partyisfull-nojoin"));
        }


        return true;
    }
}
