package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCmd extends BaseCmd {
    public EditCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "edit";
        alias = new String[]{"e"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().getSpawn() != null) {
            String worldName = args[1];
            GameMap gMap = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
            if (gMap == null) {
                sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
                return true;
            }
            GameMap.editMap(gMap, player);
            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.nospawn"));
        return true;
    }
}
