package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class IconCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public IconCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "icon";
        alias = new String[]{"i"};
        argLength = 2;
    }

    public boolean run() {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        kit.setIcon(com.walrusone.skywarsreloaded.SkyWarsReloaded.getNMS().getMainHandItem(player).clone());

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("icon", kit.getIcon().getType().toString()).format("command.kit-icon"));
        return true;
    }
}
