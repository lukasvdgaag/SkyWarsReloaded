package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CreateCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public CreateCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "create";
        alias = new String[]{"c"};
        argLength = 2;
    }

    public boolean run() {
        if (SkyWarsReloaded.getCfg().getSpawn() != null) {
            String worldName = args[1];
            Environment environment = Environment.NORMAL;
            if (args.length > 2) {
                if (args[2].equalsIgnoreCase("the_end")) {
                    environment = Environment.THE_END;
                } else if (args[2].equalsIgnoreCase("nether")) {
                    environment = Environment.NETHER;
                }
            }

            GameMap gMap = GameMap.getMap(worldName);
            if (gMap != null) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.map-exists"));
                return true;
            }
            World result = GameMap.createNewMap(worldName, environment);
            if (result == null) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.map-world-exists"));
                return true;
            }
            player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).format("maps.created"));
            gMap = GameMap.getMap(worldName);
            if (gMap != null) {
                gMap.setEditing(true);
                World editWorld = SkyWarsReloaded.get().getServer().getWorld(worldName);
                editWorld.setAutoSave(true);
                player.setGameMode(GameMode.CREATIVE);
                result.getBlockAt(0, 75, 0).setType(Material.STONE);
                player.teleport(new org.bukkit.Location(result, 0.0D, 76.0D, 0.0D), PlayerTeleportEvent.TeleportCause.PLUGIN);
                player.setAllowFlight(true);
                player.setFlying(true);
            }
            return true;
        }

        sender.sendMessage(new Messaging.MessageFormatter().format("error.nospawn"));
        return true;
    }
}
