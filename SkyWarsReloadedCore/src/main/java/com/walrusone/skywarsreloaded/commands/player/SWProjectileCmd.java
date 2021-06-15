package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWProjectileCmd extends BaseCmd {
    public SWProjectileCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "projectile";
        alias = new String[]{"proj"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.PROJECTILEEFFECT, true);
        return true;
    }
}
