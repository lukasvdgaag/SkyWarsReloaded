package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class SaveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SaveCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "save";
        alias = new String[]{"s"};
        argLength = 2;
    }

    public boolean run() {
        String worldName = args[1];
        GameMap gMap = GameMap.getMap(worldName);
        if ((gMap == null) || (!gMap.isEditing())) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
            return true;
        }
        gMap.saveMap(player);
        return true;
    }
}
