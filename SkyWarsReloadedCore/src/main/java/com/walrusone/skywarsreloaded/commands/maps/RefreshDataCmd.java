package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefreshDataCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public RefreshDataCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        this.type = t;
        this.forcePlayer = false;
        this.cmdName = "refresh";
        this.alias = new String[]{"ref"};
        this.argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String name = args[1];
        if (args[1].equalsIgnoreCase("all")) {
            for (GameMap gMap : SkyWarsReloaded.getGameMapMgr().getMapsCopy()) {
                refreshMap(gMap, sender);
            }
        } else {
            GameMap gMap = SkyWarsReloaded.getGameMapMgr().getMap(name);
            refreshMap(gMap, sender);
        }
        return true;
    }

    private void refreshMap(GameMap gMap, CommandSender sender) {
        if (gMap != null) {
            boolean reregister = gMap.isRegistered();
            if (reregister) {
                gMap.unregister(false);
                gMap.loadArenaData();
                gMap.registerMap(sender);
            } else {
                gMap.loadArenaData();
            }
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.refreshed"));
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        }
    }
}
