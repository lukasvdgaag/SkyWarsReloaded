package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;

public class SWProjectileCmd extends BaseCmd {
    public SWProjectileCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "projectile";
        alias = new String[]{"proj"};
        argLength = 1;
    }

    public boolean run() {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.PROJECTILEEFFECT, true);
        return true;
    }
}
