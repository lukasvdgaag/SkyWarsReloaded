package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class RefreshData extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public RefreshData(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "refresh";
        alias = new String[]{"ref"};
        argLength = 2;
    }

    public boolean run() {
        String name = args[1];
        if (args[1].equalsIgnoreCase("all")) {
            for (GameMap gMap : GameMap.getMaps()) {
                refreshMap(gMap);
            }
        } else {
            GameMap gMap = GameMap.getMap(name);
            refreshMap(gMap);
        }
        return true;
    }

    private void refreshMap(GameMap gMap) {
        if (gMap != null) {
            boolean reregister = gMap.isRegistered();
            if (reregister) {
                gMap.unregister(false);
                gMap.loadArenaData();
                gMap.registerMap();
            } else {
                gMap.loadArenaData();
            }
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.refreshed"));
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        }
    }
}
