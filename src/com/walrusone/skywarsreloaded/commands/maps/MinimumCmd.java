package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class MinimumCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public MinimumCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "minimum";
        alias = new String[]{"min"};
        argLength = 3;
    }

    public boolean run() {
        String worldName = args[1];
        if (!com.walrusone.skywarsreloaded.utilities.Util.get().isInteger(args[2])) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-min-be-int"));
            return true;
        }

        int min = Integer.parseInt(args[2]);
        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            map.setMinTeams(min);
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).setVariable("min", args[2]).format("maps.minplayer"));
            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
