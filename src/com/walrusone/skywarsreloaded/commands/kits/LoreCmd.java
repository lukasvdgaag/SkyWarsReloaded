package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class LoreCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LoreCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "lore";
        alias = new String[]{"l"};
        argLength = 4;
    }

    public boolean run() {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            message.append(args[i]);
            message.append(" ");
        }

        if (com.walrusone.skywarsreloaded.utilities.Util.get().isInteger(args[2])) {
            kit.setLoreLine(Integer.parseInt(args[2]), message.toString().trim());
        } else if (args[2].equalsIgnoreCase("locked")) {
            kit.setLockedLore(message.toString().trim());
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("command.kit-loreerror"));
        }

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("line", args[2]).setVariable("kit", kit.getColorName()).format("command.kit-lore"));
        return true;
    }
}
