package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionSelectionMenu;

public class SWParticleCmd extends BaseCmd {
    public SWParticleCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "particle";
        alias = new String[]{"par"};
        argLength = 1;
    }

    public boolean run() {
        new OptionSelectionMenu(player, com.walrusone.skywarsreloaded.enums.PlayerOptions.PARTICLEEFFECT, true);
        return true;
    }
}
