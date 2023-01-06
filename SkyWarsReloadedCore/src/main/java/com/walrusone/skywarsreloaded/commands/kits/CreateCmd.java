package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCmd extends BaseCmd {
    public CreateCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "create";
        alias = new String[]{"c"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit.newKit(player, args[1]);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.kit-create"));
        return true;
    }
}
