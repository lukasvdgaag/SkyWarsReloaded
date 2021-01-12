package com.walrusone.skywarsreloaded.api.impl;

import com.walrusone.skywarsreloaded.api.SWRGameAPI;
import com.walrusone.skywarsreloaded.api.SkywarsReloadedAPI;

public class SWRGameImpl implements SWRGameAPI {

    SkywarsReloadedAPI swrAPI;

    public SWRGameImpl(SkywarsReloadedAPI swrAPIIn) {
        this.swrAPI = swrAPIIn;
    }

}
