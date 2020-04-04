package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class CreatorCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public CreatorCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "creator";
        alias = new String[]{"maker"};
        argLength = 3;
    }

    public boolean run() {
        String worldName = args[1];
        StringBuilder creator = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            creator.append(args[i]);
            creator.append(" ");
        }
        creator.substring(0, creator.length() - 1);
        if (creator.length() == 0) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-creator"));
            return false;
        }
        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            map.setCreator(creator.toString());
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).setVariable("creator", args[2]).format("maps.creator"));

            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
