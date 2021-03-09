package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;

public class SWTauntCmd extends BaseCmd {
    public SWTauntCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "taunt";
        alias = new String[]{"t"};
        argLength = 1;
    }

    public boolean run() {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.TAUNT, true);
        return true;
    }
}
