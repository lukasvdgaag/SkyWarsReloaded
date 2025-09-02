package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateTopCmd extends BaseCmd {

    public UpdateTopCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = false;
        cmdName = "updatetop";
        alias = new String[]{"ut"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {
        for (LeaderType type : LeaderType.values()) {
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                DataStorage.get().updateTop(type, SkyWarsReloaded.getCfg().getLeaderSize());
            }
        }
        return true;
    }
}