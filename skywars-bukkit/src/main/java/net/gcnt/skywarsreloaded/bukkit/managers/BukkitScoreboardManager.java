package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitSWScoreboard;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.manager.AbstractScoreboardManager;
import net.gcnt.skywarsreloaded.utils.scoreboards.SWBoard;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class BukkitScoreboardManager extends AbstractScoreboardManager {

    public BukkitScoreboardManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWBoard createScoreboard(SWPlayer player, int lineCount) {
        return new BukkitSWScoreboard(plugin, (BukkitSWPlayer) player, lineCount);
    }
}
