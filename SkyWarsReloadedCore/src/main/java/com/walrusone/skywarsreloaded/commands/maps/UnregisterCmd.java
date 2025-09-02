package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnregisterCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public UnregisterCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = false;
        cmdName = "unregister";
        alias = new String[]{"unreg"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        GameMap map = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if (map != null) {
            map.unregister(true);
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.unregistered"));
            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
