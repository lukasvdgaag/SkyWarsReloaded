package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWKillsoundCmd extends BaseCmd {
    public SWKillsoundCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "killsound";
        alias = new String[]{"ks"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.KILLSOUND, true);
        return true;
    }
}
