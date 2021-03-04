package com.walrusone.skywarsreloaded.api;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;

public interface SkywarsReloadedAPI {

    SkyWarsReloaded getPlugin();

    SWRCommandAPI getCommandAPI();

    SWREventAPI getEventAPI();

    SWRGameAPI getGameAPI();

}
