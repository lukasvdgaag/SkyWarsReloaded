package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class SetSpawnCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SetSpawnCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "setspawn";
        alias = new String[]{"sspawn"};
        argLength = 1;
    }

    public boolean run() {
        org.bukkit.Location spawn = player.getLocation();
        SkyWarsReloaded.getCfg().setSpawn(spawn);
        SkyWarsReloaded.getCfg().save();
        player.sendMessage(new Messaging.MessageFormatter().format("command.spawnset"));
        return true;
    }
}
