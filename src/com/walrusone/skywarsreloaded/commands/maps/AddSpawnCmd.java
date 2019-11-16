package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Material;

public class AddSpawnCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public AddSpawnCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "spawn";
        alias = new String[]{"sp"};
        argLength = 2;
    }

    public boolean run() {
        if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().getSpawn() != null) {
            String type = args[1];
            GameMap gMap = GameMap.getMap(player.getLocation().getWorld().getName());
            if ((gMap == null) || (!gMap.isEditing())) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.map-not-editing"));
                return true;
            }
            if ((type.equalsIgnoreCase("player")) || (type.equalsIgnoreCase("p"))) {
                gMap.addTeamCard(player.getLocation());
                player.getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + gMap.getMaxPlayers()).setVariable("mapname", gMap.getDisplayName()).format("maps.addSpawn"));
            } else if ((type.equalsIgnoreCase("spec")) || (type.equalsIgnoreCase("s"))) {
                gMap.setSpectateSpawn(player.getLocation());
                player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.specSpawn"));
            } else if ((type.equalsIgnoreCase("deathmatch")) || (type.equalsIgnoreCase("dm")) || (type.equalsIgnoreCase("d"))) {
                gMap.addDeathMatchSpawn(player.getLocation());
                player.getLocation().getBlock().setType(Material.EMERALD_BLOCK);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + gMap.getDeathMatchSpawns().size()).setVariable("mapname", gMap.getDisplayName()).format("maps.addDeathSpawn"));
            } else if ((type.equalsIgnoreCase("look")) || type.equals("eye")) {
                gMap.setLookDirection(player.getLocation());
                player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.setLookDirection"));
            } else {
                player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Type must be: player, spec, eye or deathmatch");
            }
            return true;
        }
        return true;
    }
}
