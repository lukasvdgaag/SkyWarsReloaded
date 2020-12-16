package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.Bukkit;

import java.util.StringJoiner;
import java.util.UUID;

public class InfoCmd extends BaseCmd {
    public InfoCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "info";
        alias = new String[]{"in"};
        argLength = 1;
    }

    public boolean run() {
        Party party = Party.getParty(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.notinaparty"));
            return true;
        }

        player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.info1"));
        player.sendMessage(new Messaging.MessageFormatter().setVariable("leader", Bukkit.getPlayer(party.getLeader()).getName()).format("party.info2"));
        StringJoiner members = new StringJoiner(", ");
        for (UUID uuid : party.getMembers()) {
            members.add(Bukkit.getPlayer(uuid).getName());
        }
        player.sendMessage(new Messaging.MessageFormatter().setVariable("members", members.toString()).format("party.info3"));
        return true;
    }
}
