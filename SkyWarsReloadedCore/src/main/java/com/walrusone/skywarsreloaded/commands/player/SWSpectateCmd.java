package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWSpectateCmd extends BaseCmd {
    public SWSpectateCmd(String t) {
        this.type = t;
        this.forcePlayer = true;
        this.cmdName = "spectate";
        this.alias = new String[] {"spec"};
        this.argLength = 2;
    }


    public boolean run(CommandSender sender, Player player, String[] args) {
        GameMap gMap = GameMap.getMap(ChatColor.stripColor(args[1]));
        if (gMap != null) {
            sendSpectator(gMap, player);
            return true;
        }
        gMap = GameMap.getMapByDisplayName(ChatColor.stripColor(args[1]));
        if (gMap != null) {
            sendSpectator(gMap, player);
            return true;
        }
        Player swPlayer = null;
        for (Player playerMatch : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
                swPlayer = playerMatch;
            }
        }
        if (swPlayer != null) {
            gMap = MatchManager.get().getPlayerMap(swPlayer);
            sendSpectator(gMap, player);
            return true;
        }


        return true;
    }

    private void sendSpectator(GameMap gMap, Player player) {
        if (gMap != null) {
            if ((gMap.getMatchState() == MatchState.WAITINGSTART) || (gMap.getMatchState() == MatchState.WAITINGLOBBY) ||(gMap.getMatchState() == MatchState.PLAYING)) {
                SkyWarsReloaded.get().getPlayerManager().addSpectator(gMap, player);
            } else {
                player.sendMessage(new Messaging.MessageFormatter().format("error.spectate-notatthistime"));
            }
        }
    }
}
