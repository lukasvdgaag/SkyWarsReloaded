package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.manager.AbstractPlayerManager;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
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
