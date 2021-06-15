package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.menus.playeroptions.OptionsSelectionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWOptionsCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SWOptionsCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "options";
        alias = new String[]{"o"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        new OptionsSelectionMenu(player);
        return true;
    }
}
