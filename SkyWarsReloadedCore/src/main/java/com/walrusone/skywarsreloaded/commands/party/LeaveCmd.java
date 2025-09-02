package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LeaveCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "leave";
        alias = new String[]{"l"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
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
