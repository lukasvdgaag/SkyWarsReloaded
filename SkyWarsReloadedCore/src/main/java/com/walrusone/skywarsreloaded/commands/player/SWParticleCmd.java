package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWParticleCmd extends BaseCmd {
    public SWParticleCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "particle";
        alias = new String[]{"par"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.PARTICLEEFFECT, true);
        return true;
    }
}
