package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCmd extends BaseCmd {
    public StartCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "start";
        alias = new String[]{"s"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        com.walrusone.skywarsreloaded.game.GameMap gMap = MatchManager.get().getPlayerMap(player);
        if (gMap != null) {
            MatchManager.get().forceStart(player);
        }
        return true;
    }
}
