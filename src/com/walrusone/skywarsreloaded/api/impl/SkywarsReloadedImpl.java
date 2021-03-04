package com.walrusone.skywarsreloaded.api.impl;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.api.SWRCommandAPI;
import com.walrusone.skywarsreloaded.api.SWREventAPI;
import com.walrusone.skywarsreloaded.api.SWRGameAPI;
import com.walrusone.skywarsreloaded.api.SkywarsReloadedAPI;

public class SkywarsReloadedImpl implements SkywarsReloadedAPI {


    private final SkyWarsReloaded plugin;
    private SWRCommandAPI swrCmdAPI;
    private SWREventAPI swrEventAPI;
    private SWRGameAPI swrGameAPI;

    public SkywarsReloadedImpl() {
        this.plugin = SkyWarsReloaded.get();
        this.swrGameAPI = new SWRGameImpl(this);
    }

    @Override
    public SkyWarsReloaded getPlugin() {
        return this.plugin;
    }

    @Override
    public SWRCommandAPI getCommandAPI() {
        return null;
    }

    @Override
    public SWREventAPI getEventAPI() {
        return null;
    }

    @Override
    public SWRGameAPI getGameAPI() {
        return this.swrGameAPI;
    }

}
