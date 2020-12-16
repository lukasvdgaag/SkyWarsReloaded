package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;

public class DisbandCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public DisbandCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "disband";
        alias = new String[]{"dis"};
        argLength = 1;
    }

    public boolean run() {
        Party party = Party.getParty(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.notinaparty"));
            return true;
        }

        if (!party.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.mustbepartyleader"));
            return true;
        }

        Party.removeParty(party);
        return true;
    }
}
