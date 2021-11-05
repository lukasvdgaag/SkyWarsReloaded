package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;

public class CoreSWPlayerDataManager implements SWPlayerDataManager {

    private AbstractSkyWarsReloaded skyWars;

    public CoreSWPlayerDataManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.skyWars = skyWarsIn;
    }

    @Override
    public SWPlayerData createSWPlayerDataInstance() {
        return new CoreSWPlayerData();
    }
}
