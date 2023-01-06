package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWGlassCmd extends BaseCmd {
    public SWGlassCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "glass";
        alias = new String[]{"g"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.GLASSCOLOR, true);
        return true;
    }
}
