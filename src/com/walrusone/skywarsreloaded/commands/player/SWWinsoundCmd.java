package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;

public class SWWinsoundCmd extends BaseCmd {
    public SWWinsoundCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "winsound";
        alias = new String[]{"ws"};
        argLength = 1;
    }

    public boolean run() {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.WINSOUND, true);
        return true;
    }
}
