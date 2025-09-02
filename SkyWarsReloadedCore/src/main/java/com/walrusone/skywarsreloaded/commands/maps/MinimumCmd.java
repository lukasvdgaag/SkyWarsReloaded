package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinimumCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public MinimumCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = false;
        cmdName = "minimum";
        alias = new String[]{"min"};
        argLength = 3;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        if (!com.walrusone.skywarsreloaded.utilities.Util.get().isInteger(args[2])) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-min-be-int"));
            return true;
        }

        int min = Integer.parseInt(args[2]);
        GameMap map = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if (map != null) {
            map.setMinTeams(min);
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).setVariable("min", args[2]).format("maps.minplayer"));
            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
