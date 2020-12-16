package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.game.GameMap;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCmd
        extends BaseCmd {
    public ArenaCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "arenas";
        alias = new String[]{"a"};
        argLength = 1;
    }

    public boolean run() {
        GameMap.openArenasManager(player);
        new BukkitRunnable() {
            public void run() {
                GameMap.updateArenasManager();
            }
        }.runTaskLaterAsynchronously(SkyWarsReloaded.get(), 2L);
        return true;
    }
}
