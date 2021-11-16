package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.manager.AbstractPlayerManager;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.UUID;

public class BukkitPlayerManager extends AbstractPlayerManager {

    public BukkitPlayerManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWPlayer createSWPlayerForPlatform(UUID uuid) {
        return new BukkitSWPlayer(plugin, uuid, true);
    }
}
