package net.gcnt.skywarsreloaded.utils.scoreboards;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.HashMap;

public abstract class AbstractSWBoard implements SWBoard {

    protected final SWPlayer player;
    protected final int lineCount;
    protected final HashMap<Integer, String> cache;
    protected final SkyWarsReloaded plugin;

    public AbstractSWBoard(SkyWarsReloaded plugin, SWPlayer player, int lineCount) {
        this.player = player;
        this.lineCount = lineCount;
        this.cache = new HashMap<>();
        this.plugin = plugin;
    }

    @Override
    public int getLineCount() {
        return lineCount;
    }

    @Override
    public String getLine(int line) {
        return cache.getOrDefault(line, null);
    }

    @Override
    public SWPlayer getPlayer() {
        return player;
    }

}
