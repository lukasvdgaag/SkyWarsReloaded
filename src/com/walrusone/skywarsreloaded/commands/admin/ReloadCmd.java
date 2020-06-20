package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Location;

import java.util.List;

public class ReloadCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public ReloadCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "reload";
        alias = new String[]{"r"};
        argLength = 1;
    }

    public boolean run() {
        SkyWarsReloaded.get().onDisable();
        SkyWarsReloaded.get().load();

        if (SkyWarsReloaded.getCfg().bungeeMode()) {
            SkyWarsReloaded.get().prepareServers();

            for (SWRServer server : SWRServer.getServers()) {
                server.clearSigns();
                List<String> signLocs = SkyWarsReloaded.get().getConfig().getStringList("signs." + server.getServerName());
                if (signLocs != null) {
                    for (String sign : signLocs) {
                        Location loc = Util.get().stringToLocation(sign);
                        server.addSign(loc);
                    }
                }
            }
        }

        sender.sendMessage(new Messaging.MessageFormatter().format("command.reload"));
        return true;
    }
}
