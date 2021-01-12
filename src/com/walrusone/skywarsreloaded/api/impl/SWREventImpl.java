package com.walrusone.skywarsreloaded.api.impl;

import com.walrusone.skywarsreloaded.api.SWREventAPI;
import com.walrusone.skywarsreloaded.api.SkywarsReloadedAPI;

public class SWREventImpl implements SWREventAPI {

    SkywarsReloadedAPI swrAPI;

    public SWREventImpl(SkywarsReloadedAPI swrAPIIn) {
        this.swrAPI = swrAPIIn;
    }
    
}
