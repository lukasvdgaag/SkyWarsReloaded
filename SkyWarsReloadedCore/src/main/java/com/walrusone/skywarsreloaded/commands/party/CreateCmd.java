package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public CreateCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "create";
        alias = new String[]{"c"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String partyName;
        if (args.length > 1) {
            partyName = args[1];
        } else {
            partyName = player.getName();
        }

        Party party = Party.getParty(player);
        if (party != null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.alreadyinparty"));
            return true;
        }

        new Party(player, partyName);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", partyName).format("party.create"));
        return true;
    }
}
