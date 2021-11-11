package net.gcnt.skywarsreloaded.bukkit.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.AbstractChestManager;
import net.gcnt.skywarsreloaded.game.chest.SWChestType;

public class BukkitChestManager extends AbstractChestManager {

    public BukkitChestManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWChestType initChestType(String name) {
        return new BukkitSWChestType(plugin, name);
    }
}