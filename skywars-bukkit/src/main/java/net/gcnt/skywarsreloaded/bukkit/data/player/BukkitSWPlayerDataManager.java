package net.gcnt.skywarsreloaded.bukkit.data.player;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.AbstractSWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;

public class BukkitSWPlayerDataManager extends AbstractSWPlayerDataManager {

    public BukkitSWPlayerDataManager(BukkitSkyWarsReloaded skyWarsIn) {
        super(skyWarsIn);
    }

    @Override
    public SWPlayerData createSWPlayerDataInstance() {
        return new BukkitSWPlayerData();
    }
}
