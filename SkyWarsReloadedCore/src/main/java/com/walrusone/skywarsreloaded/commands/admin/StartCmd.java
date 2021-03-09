package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.managers.MatchManager;

public class StartCmd extends BaseCmd {
    public StartCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "start";
        alias = new String[]{"s"};
        argLength = 1;
    }

    public boolean run() {
        com.walrusone.skywarsreloaded.game.GameMap gMap = MatchManager.get().getPlayerMap(player);
        if (gMap != null) {
            MatchManager.get().forceStart(player);
        }
        return true;
    }
}
