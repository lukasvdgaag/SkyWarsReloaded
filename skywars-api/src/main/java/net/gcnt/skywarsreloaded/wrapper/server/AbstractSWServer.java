package net.gcnt.skywarsreloaded.wrapper.server;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

public abstract class AbstractSWServer implements SWServer{

    protected final SkyWarsReloaded plugin;

    public AbstractSWServer(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
    }


}
