package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CheckChestTypeCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public CheckChestTypeCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "checkchest";
        alias = new String[]{"checkchesttype"};
        argLength = 2;
    }

    public boolean run() {
        String worldName = args[1];
        GameMap map = GameMap.getMap(worldName);

        if (map == null) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
            return true;
        }

        Block block = player.getTargetBlock(null, 5);
        if (block == null || (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST)) {
            // error
            sender.sendMessage(new Messaging.MessageFormatter().format("error.not-looking-at-chest"));
            return true;
        }

        CoordLoc loc = new CoordLoc(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());

        if (map.getCenterChests().contains(loc)) {
            player.sendMessage("§aThe chest you are looking at is a §bcenter chest§a!");
            return true;
        } else if (map.getChests().contains(loc)) {
            player.sendMessage("§aThe chest you are looking at is a §bnormal chest§a!");
            return true;
        }
        player.sendMessage("§cThe chest you are looking at is not registered in this arena!");
        return true;
    }
}
