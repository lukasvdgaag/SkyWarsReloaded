package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.menus.playeroptions.OptionsSelectionMenu;

public class SWOptionsCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SWOptionsCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "options";
        alias = new String[]{"o"};
        argLength = 1;
    }

    public boolean run() {
        new OptionsSelectionMenu(player);
        return true;
    }
}
