package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.events.SkyWarsReloadEvent;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

            SWRServer.updateServerSigns();
        }

        sender.sendMessage(new Messaging.MessageFormatter().format("command.reload"));
        if (SkyWarsReloaded.get().getUpdater().getUpdateStatus() == 1) {
            BaseComponent base = new TextComponent("§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!");
            base.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SkyWarsReloaded.get().getUpdater().getUpdateURL()));
            base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§7Click here to update to the latest version!")}));
            if (sender instanceof Player) {
                SkyWarsReloaded.getNMS().sendJSON((Player)sender, "[\"\",{\"text\":\"§d§l[SkyWarsReloaded] §aA new update has been found: §b" + SkyWarsReloaded.get().getUpdater().getLatestVersion() + "§a. Click here to update!\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + SkyWarsReloaded.get().getUpdater().getUpdateURL() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7Click here to update to the latest version!\"}]}}}]");
            }
            else {
                sender.sendMessage(base.toPlainText());
            }
        }

        Bukkit.getPluginManager().callEvent(new SkyWarsReloadEvent());
        return true;
    }
}
