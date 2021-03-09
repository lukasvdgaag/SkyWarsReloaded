package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.utilities.Messaging;

public class LoadCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LoadCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "load";
        alias = new String[]{"lo"};
        argLength = 2;
    }

    public boolean run() {
        com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit kit = com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit.giveKit(player, kit);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.kit-load"));
        player.sendMessage(new Messaging.MessageFormatter().format("command.kit-loadmsg"));
        return true;
    }
}
