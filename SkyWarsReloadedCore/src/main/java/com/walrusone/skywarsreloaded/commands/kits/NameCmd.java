package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class NameCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public NameCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "name";
        alias = new String[]{"n"};
        argLength = 3;
    }

    public boolean run() {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            message.append(args[i]);
            message.append(" ");
        }

        kit.setName(message.toString().trim());

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).format("command.kit-name"));
        return true;
    }
}
