package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class RegisterCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public RegisterCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "register";
        alias = new String[]{"reg"};
        argLength = 2;
    }

    public boolean run() {
        String worldName = args[1];
        GameMap gMap = GameMap.getMap(worldName);
        if (gMap != null) {
            gMap.setRegistered(true);
            boolean registered = gMap.registerMap();
            if (registered) {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.registered"));
            } else {
                sender.sendMessage(new Messaging.MessageFormatter().format("error.map-failed-to-register"));
            }
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-register-not-exist"));
        }
        return false;
    }
}
