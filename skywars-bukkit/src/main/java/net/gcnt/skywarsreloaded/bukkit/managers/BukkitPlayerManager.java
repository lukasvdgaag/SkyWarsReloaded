package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.manager.AbstractPlayerManager;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitPlayerManager extends AbstractPlayerManager {

    private final SkyWarsReloaded plugin;
    private final List<SWPlayer> players;

    public BukkitPlayerManager(SkyWarsReloaded plugin) {
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

        swp = new BukkitSWPlayer(plugin, uuid, true);
        players.add(swp);
        return swp;
    }

    @Override
    public List<SWPlayer> getPlayers() {
        return players;
    }
}
