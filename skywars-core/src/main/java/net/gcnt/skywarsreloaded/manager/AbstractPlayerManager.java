package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractPlayerManager implements SWPlayerManager {

    public final SkyWarsReloaded plugin;
    private final List<SWPlayer> players;

    public AbstractPlayerManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
    }

    @Override
    public SWPlayer getPlayerByUUID(UUID uuid) {
        synchronized (players) {
            for (SWPlayer swp : players) {
                if (swp.getUuid().equals(uuid)) return swp;
            }
        }
        return null;
    }

    @Override
    public SWPlayer initPlayer(UUID uuid) {
        SWPlayer swp = getPlayerByUUID(uuid);
        if (swp != null) return swp;

        swp = this.createSWPlayerForPlatform(uuid);
        // load data
        players.add(swp);
        return swp;
    }

    @Override
    public List<SWPlayer> getPlayers() {
        return players;
    }

    @Override
    public void removePlayer(SWPlayer player) {
        this.players.remove(player);
    }

    // Platform specific

    public abstract SWPlayer createSWPlayerForPlatform(UUID uuid);
}
