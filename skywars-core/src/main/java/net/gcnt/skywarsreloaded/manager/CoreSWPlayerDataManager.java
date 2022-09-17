package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.Storage;
import net.gcnt.skywarsreloaded.data.player.CoreSWPlayerData;
import net.gcnt.skywarsreloaded.data.player.CoreSWPlayerStats;
import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.SWPlayerStats;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreSWPlayerDataManager implements SWPlayerDataManager {

    private final SkyWarsReloaded plugin;

    public CoreSWPlayerDataManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.plugin = skyWarsIn;
    }

    @Override
    public SWPlayerData createSWPlayerDataInstance() {
        return new CoreSWPlayerData();
    }

    @Override
    public SWPlayerStats createSWPlayerStatsInstance() {
        return new CoreSWPlayerStats();
    }

    @Override
    public void loadAllPlayerData() {
        final Storage storage = this.plugin.getStorage();
        for (SWPlayer player : this.plugin.getPlayerManager().getPlayers()) {
            storage.loadData(player);
        }
    }
}
