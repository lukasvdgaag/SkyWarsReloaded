package net.gcnt.skywarsreloaded.bukkit.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.AbstractChestManager;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;

public class BukkitChestManager extends AbstractChestManager {

    public BukkitChestManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWChestTier initChestTier(String name) {
        return new BukkitSWChestTier(plugin, name);
    }
}