package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;

public abstract class AbstractSWPlayerDataManager implements SWPlayerDataManager {

    private AbstractSkyWarsReloaded skyWars;

    public AbstractSWPlayerDataManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.skyWars = skyWarsIn;
    }
}
