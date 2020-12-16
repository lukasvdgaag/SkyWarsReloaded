package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;

public class LeaveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LeaveCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "leave";
        alias = new String[]{"l"};
        argLength = 1;
    }

    public boolean run() {
        Party party = Party.getParty(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.notinaparty"));
            return true;
        }

        party.removeMember(player);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.youlefttheparty"));
        return true;
    }
}
