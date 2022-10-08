package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.CoreSWPlayerData;
import net.gcnt.skywarsreloaded.data.player.CoreSWPlayerStats;
import net.gcnt.skywarsreloaded.data.player.CoreSWPlayerUnlockables;
import net.gcnt.skywarsreloaded.data.player.SWPlayerStorageUnit;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerStats;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerUnlockables;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreSWPlayerDataManager implements SWPlayerDataManager {

    private final SkyWarsReloaded plugin;

    public CoreSWPlayerDataManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.plugin = skyWarsIn;
    }

    @Override
    public SWPlayerData createSWPlayerDataInstance(SWPlayer player) {
        return new CoreSWPlayerData(plugin, player);
    }

    @Override
    public SWPlayerStats createSWPlayerStatsInstance() {
        return new CoreSWPlayerStats();
    }

    @Override
    public SWPlayerUnlockables createSWPlayerUnlockablesInstance(SWPlayer player) {
        return new CoreSWPlayerUnlockables(plugin, player);
    }

    @Override
    public void loadAllPlayerData() {
        final SWPlayerStorageUnit storage = this.plugin.getPlayerStorage();
        for (SWPlayer player : this.plugin.getPlayerManager().getPlayers()) {
            storage.loadData(player);
        }
    }
}
