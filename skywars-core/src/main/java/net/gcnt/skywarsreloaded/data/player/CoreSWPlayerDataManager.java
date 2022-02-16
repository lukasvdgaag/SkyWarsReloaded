package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWPlayerDataManager implements SWPlayerDataManager {

    private final SkyWarsReloaded plugin;

    public CoreSWPlayerDataManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.plugin = skyWarsIn;
    }

    @Override
    public SWPlayerData createSWPlayerDataInstance() {
        return new CoreSWPlayerData();
    }
}
