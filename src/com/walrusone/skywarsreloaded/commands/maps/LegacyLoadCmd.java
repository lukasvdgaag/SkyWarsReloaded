package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class LegacyLoadCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LegacyLoadCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "legacyload";
        alias = new String[]{"ll"};
        argLength = 2;
    }

    public boolean run() {
        String worldName = args[1];
        GameMap gMap = GameMap.getMap(worldName);
        if (gMap != null) {
            gMap.scanWorld(true, player);
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-register-not-exist"));
        }
        return true;
    }
}
