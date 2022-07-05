package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;

public abstract class CoreScoreboardManager implements ScoreboardManager {

    protected final SkyWarsReloaded plugin;

    public CoreScoreboardManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public String determineScoreboardFormat(SWPlayer player) {
        // todo get game from player.
        return "lobby";
    }

    @Override
    public List<String> getScoreboardLines(SWPlayer player, String format) {
        return null;
    }
}
