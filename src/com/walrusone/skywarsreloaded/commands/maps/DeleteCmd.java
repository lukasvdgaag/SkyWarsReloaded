package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class DeleteCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public DeleteCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "delete";
        alias = new String[]{"d", "remove"};
        argLength = 2;
    }

    public boolean run() {
        String worldName = args[1];
        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            boolean result = map.removeMap();
            if (result) {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).format("maps.deleted"));
                return true;
            }
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-remove"));
            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
