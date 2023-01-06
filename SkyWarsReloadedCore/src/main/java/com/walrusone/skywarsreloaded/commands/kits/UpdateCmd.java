package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public UpdateCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "update";
        alias = new String[]{"u"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        kit.setArmor(player.getInventory().getArmorContents());
        kit.setInventory(player.getInventory().getContents());

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).format("command.kit-update"));
        return true;
    }
}
