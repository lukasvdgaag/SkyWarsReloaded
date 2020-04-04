package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class PermCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public PermCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "perm";
        alias = new String[]{"p"};
        argLength = 2;
    }

    public boolean run() {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        String message;
        if (kit.needPermission()) {
            kit.setNeedPermission(false);
            message = "no longer";
        } else {
            kit.setNeedPermission(true);
            message = "now";
        }

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).setVariable("state", message).format("command.kit-perm"));
        return true;
    }
}
