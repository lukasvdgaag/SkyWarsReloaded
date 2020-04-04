package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.enums.LeaderType;

public class UpdateTopCmd extends BaseCmd {

    public UpdateTopCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "updatetop";
        alias = new String[]{"ut"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        for (LeaderType type : LeaderType.values()) {
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                DataStorage.get().updateTop(type, SkyWarsReloaded.getCfg().getLeaderSize());
            }
        }
        return true;
    }
}