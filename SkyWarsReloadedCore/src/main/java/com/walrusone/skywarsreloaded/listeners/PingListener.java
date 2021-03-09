package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements org.bukkit.event.Listener {
    public PingListener() {
    }

    @org.bukkit.event.EventHandler
    public void onPing(ServerListPingEvent serverListPingEvent) {
        if (SkyWarsReloaded.get().serverLoaded()) {
            if (GameMap.getMaps().size() > 0) {
                GameMap game = (GameMap) GameMap.getMaps().get(0);
                serverListPingEvent.setMotd(new Messaging.MessageFormatter().setVariable("matchstate", game.getMatchState().toString())
                        .setVariable("playercount", "" + game.getPlayerCount()).setVariable("maxplayers", "" + game.getMaxPlayers())
                        .setVariable("displayname", game.getDisplayName()).format("bungee.motd"));
            } else {
                serverListPingEvent.setMotd(new Messaging.MessageFormatter().setVariable("matchstate", MatchState.ENDING.toString())
                        .setVariable("playercount", "0").setVariable("maxplayers", "0")
                        .setVariable("displayname", "null").format("bungee.motd"));
            }
        } else {
            serverListPingEvent.setMotd(new Messaging.MessageFormatter().setVariable("matchstate", MatchState.ENDING.toString())
                    .setVariable("playercount", "0").setVariable("maxplayers", "0")
                    .setVariable("displayname", "null").format("bungee.motd"));
        }
    }

}
