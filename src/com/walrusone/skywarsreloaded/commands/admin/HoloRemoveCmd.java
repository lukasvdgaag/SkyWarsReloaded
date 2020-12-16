package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;

public class HoloRemoveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public HoloRemoveCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "holoremove";
        alias = new String[]{"hr", "removeholo", "removehologram", "hologramremove"};
        argLength = 1;
    }

    public boolean run() {
        if (SkyWarsReloaded.getCfg().hologramsEnabled()) {
            boolean result = SkyWarsReloaded.getHoloManager().removeHologram(player.getLocation());
            if (result) {
                player.sendMessage(new Messaging.MessageFormatter().format("command.hologram-removed"));
                return true;
            }
            player.sendMessage(new Messaging.MessageFormatter().format("error.no-holograms-found"));
            return true;
        }
        player.sendMessage(new Messaging.MessageFormatter().format("error.holograms-not-enabled"));
        return true;
    }
}
