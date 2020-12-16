package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;

public class TeamSizeCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public TeamSizeCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "teamsize";
        alias = new String[]{"team"};
        argLength = 3;
    }

    public boolean run() {
        String worldName = args[1];
        if (!com.walrusone.skywarsreloaded.utilities.Util.get().isInteger(args[2])) {
            sender.sendMessage(new MessageFormatter().format("error.map-min-be-int"));
            return true;
        }

        int min = Integer.parseInt(args[2]);
        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            map.setTeamSize(min);
            sender.sendMessage(new MessageFormatter().setVariable("mapname", worldName).setVariable("size", args[2]).format("maps.teamsize"));
            return true;
        }
        sender.sendMessage(new MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
