package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.ChatColor;

public class RegisterCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public RegisterCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "register";
        alias = new String[]{"reg"};
        argLength = 2;
    }

    public boolean run() {
        String worldName = args[1];
        GameMap gMap = GameMap.getMap(worldName);
        if (gMap != null) {
            gMap.setRegistered(true);
            int registeredStatus = gMap.registerMap();
            if (registeredStatus == 0) {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.registered"));
            } else {
                if (registeredStatus == 1) {
                    sender.sendMessage(ChatColor.RED + "Could not register the map " + gMap.getName() + ". The team spawns are unbalanced. Make sure that each team has the same amount of spawns and that they do not override the team size.");
                }
                else if (registeredStatus == 2) {
                    sender.sendMessage(ChatColor.RED + "Could not register the map " + gMap.getName() + ". The arena doesn't have enough spawnpoints, you must have at least 2 set. Use '/swm spawn player'.");
                }
                else if (registeredStatus == 3) {
                    sender.sendMessage(ChatColor.RED + "Could not register the map " + gMap.getName() + ". You didn't set the spectator spawn. Use '/swm spawn spec'.");
                }
                else if (registeredStatus == 4) {
                    sender.sendMessage(ChatColor.RED + "Could not register the map " + gMap.getName() + ". No waiting lobby spawn has been set. This is required for team games. Use '/swm spawn lobby'.");
                }
                else {
                    sender.sendMessage(new Messaging.MessageFormatter().format("error.map-failed-to-register"));
                }
            }
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-register-not-exist"));
        }
        return true;
    }
}
