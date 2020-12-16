package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import com.walrusone.skywarsreloaded.utilities.Util;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

            for (SWRServer server : SWRServer.getServersCopy()) {
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
        if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1) {
            BaseComponent base = new TextComponent("§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!");
            base.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SkyWarsReloaded.get().getUpdater().getUpdateURL()));
            base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§7Click here to update to the latest version!")}));
            sender.spigot().sendMessage(base);
        }
        return true;
    }
}
