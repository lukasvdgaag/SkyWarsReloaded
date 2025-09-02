package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.ChestPlacementType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestTypeCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public ChestTypeCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = false;
        cmdName = "chesttype";
        alias = new String[]{"ct"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        GameMap map = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if (map != null) {
            if (map.getChestPlacementType() == ChestPlacementType.NORMAL) {
                map.setChestPlacementType(ChestPlacementType.CENTER);
            } else {
                map.setChestPlacementType(ChestPlacementType.NORMAL);
            }
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName)
                    .setVariable("type", map.getChestPlacementType().toString())
                    .format("maps.chestPlacementType"));
            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
