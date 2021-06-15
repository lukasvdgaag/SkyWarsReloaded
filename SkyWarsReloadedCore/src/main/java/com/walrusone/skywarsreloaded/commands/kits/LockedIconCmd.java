package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockedIconCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LockedIconCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "lockicon";
        alias = new String[]{"locki"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        kit.setLIcon(com.walrusone.skywarsreloaded.SkyWarsReloaded.getNMS().getMainHandItem(player).clone());

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("icon", kit.getIcon().getType().toString()).format("command.kit-locked-icon"));
        return true;
    }
}
