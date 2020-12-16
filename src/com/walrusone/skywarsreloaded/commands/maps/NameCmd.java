package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class NameCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public NameCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "name";
        alias = new String[]{"n"};
        argLength = 3;
    }

    public boolean run() {
        String worldName = args[1];

        StringBuilder b = new StringBuilder();
        for (int i = 2;i<args.length;i++) {
            b.append(i > 2 ? " " + args[i] : args[i]);
        }
        String displayName = b.toString();

        if (displayName.length() == 0) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-name"));
            return true;
        }

        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            map.setDisplayName(displayName.trim());
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).setVariable("displayname", displayName.trim()).format("maps.name"));

            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
