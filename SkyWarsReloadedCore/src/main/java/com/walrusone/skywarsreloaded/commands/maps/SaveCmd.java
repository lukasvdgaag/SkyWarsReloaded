package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SaveCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "save";
        alias = new String[]{"s"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        GameMap gMap = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if ((gMap == null) || (!gMap.isEditing())) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
            return true;
        }
        gMap.saveMap(player);
        return true;
    }
}
