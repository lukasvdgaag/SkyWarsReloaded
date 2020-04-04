package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class EnableCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public EnableCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "enable";
        alias = new String[]{"e"};
        argLength = 2;
    }

    public boolean run() {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        String message;
        if (kit.getEnabled()) {
            kit.setEnabled(false);
            message = "disabled";
        } else {
            kit.setEnabled(true);
            message = "enabled";
        }

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).setVariable("state", message).format("command.kit-enable"));
        return true;
    }
}
