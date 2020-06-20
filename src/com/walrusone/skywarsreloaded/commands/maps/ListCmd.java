package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.ChatColor;

public class ListCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public ListCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "list";
        alias = new String[]{"l"};
        argLength = 1;
    }

    public boolean run() {
        sender.sendMessage(new Messaging.MessageFormatter().format("maps.listHeader"));
        for (GameMap map : GameMap.getMaps()) {
            if (map.isRegistered()) {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("filename", map.getName()).setVariable("displayname", map.getDisplayName()).setVariable("status", ChatColor.GREEN + "REGISTERED").format("maps.listResult"));
            } else {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("filename", map.getName()).setVariable("displayname", map.getDisplayName()).setVariable("status", ChatColor.RED + "UNREGISTERED").format("maps.listResult"));
            }
        }
        return true;
    }
}
