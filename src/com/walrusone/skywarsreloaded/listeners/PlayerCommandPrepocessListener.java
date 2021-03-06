package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPrepocessListener implements org.bukkit.event.Listener {
    public PlayerCommandPrepocessListener() {
    }

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPrepocess(PlayerCommandPreprocessEvent e) {
        com.walrusone.skywarsreloaded.game.GameMap gMap = MatchManager.get().getSpectatorMap(e.getPlayer());
        String[] splited = e.getMessage().split("\\s+");
        if (gMap != null) {
            if (splited[0].equalsIgnoreCase("/spawn")) {
                e.setCancelled(true);
                SkyWarsReloaded.get().getPlayerManager().removePlayer(
                        e.getPlayer(), PlayerRemoveReason.PLAYER_QUIT_GAME, null, false
                );
                return;
            }
            if (SkyWarsReloaded.getCfg().disableCommandsSpectate()) {
                if (e.getPlayer().hasPermission("sw.allowcommands")) {
                    return;
                }
                for (String a1 : SkyWarsReloaded.getCfg().getEnabledCommandsSpectate()) {
                    if (splited.length == 1) {
                        if (!splited[0].equalsIgnoreCase("/" + a1)) {
                        }


                    } else if ((splited[0].equalsIgnoreCase("/" + a1)) || ((splited[0] + " " + splited[1]).equalsIgnoreCase("/" + a1))) {
                        return;
                    }
                }

                e.getPlayer().sendMessage(new Messaging.MessageFormatter().format("game.command-disabled-spec"));
                e.setCancelled(true);
                return;
            }
        }

        if (MatchManager.get().getPlayerMap(e.getPlayer()) != null) {
            if (e.getPlayer().hasPermission("sw.allowcommands")) {
                return;
            }
            for (String a1 : SkyWarsReloaded.getCfg().getEnabledCommands()) {
                if (splited.length == 1) {
                    if (!splited[0].equalsIgnoreCase("/" + a1)) {
                    }

                } else if ((splited.length > 1) && (
                        (splited[0].equalsIgnoreCase("/" + a1)) || ((splited[0] + " " + splited[1]).equalsIgnoreCase("/" + a1)))) {
                    return;
                }
            }

            e.getPlayer().sendMessage(new Messaging.MessageFormatter().format("game.command-disabled"));
            e.setCancelled(true);
        }
    }
}
