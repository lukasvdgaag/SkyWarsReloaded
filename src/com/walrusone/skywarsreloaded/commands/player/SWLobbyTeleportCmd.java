package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class SWLobbyTeleportCmd extends BaseCmd {
    public SWLobbyTeleportCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "lobby";
        alias = new String[]{"spawn"};
        argLength = 1;
    }

    public boolean run() {
        if (SkyWarsReloaded.getCfg().getSpawn() != null) {
            player.teleport(SkyWarsReloaded.getCfg().getSpawn());
        } else {
            new Messaging.MessageFormatter().format("nospawn");
        }
        return true;
    }
}
