package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCmd extends BaseCmd {
    public ListCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "list";
        alias = new String[]{"li"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (GameKit.getKits().isEmpty()) {
            player.sendMessage(new Messaging.MessageFormatter().format("command.kit-listno"));
            return true;
        }
        player.sendMessage(new Messaging.MessageFormatter().format("command.kit-listheader"));
        player.sendMessage(new Messaging.MessageFormatter().format("command.kit-listheader2"));
        for (GameKit kit : GameKit.getKits()) {
            if ((!kit.getName().equalsIgnoreCase(new Messaging.MessageFormatter().format("kit.vote-random"))) &&
                    (!kit.getName().equalsIgnoreCase(new Messaging.MessageFormatter().format("kit.vote-nokit")))) {
                String message;
                if (kit.getEnabled()) {
                    message = ChatColor.GREEN + "enabled";
                } else {
                    message = ChatColor.RED + "disabled";
                }
                player.sendMessage(new Messaging.MessageFormatter().setVariable("filename", kit.getFilename())
                        .setVariable("position", "" + kit.getPosition()).setVariable("status", message).format("command.kit-list"));
            }
        }

        return true;
    }
}
