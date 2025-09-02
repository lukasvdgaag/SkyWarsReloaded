package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LegacyLoadCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public LegacyLoadCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "legacyload";
        alias = new String[]{"ll"};
        argLength = 2;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        GameMap gMap = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if (gMap != null) {
            gMap.scanWorld(true, player);
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-register-not-exist"));
        }
        return true;
    }
}
