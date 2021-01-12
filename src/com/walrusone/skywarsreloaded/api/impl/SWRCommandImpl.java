package com.walrusone.skywarsreloaded.api.impl;

import com.walrusone.skywarsreloaded.api.SWRCommandAPI;
import com.walrusone.skywarsreloaded.api.SkywarsReloadedAPI;
import com.walrusone.skywarsreloaded.commands.KitCmdManager;
import com.walrusone.skywarsreloaded.commands.MainCmdManager;
import com.walrusone.skywarsreloaded.commands.MapCmdManager;
import com.walrusone.skywarsreloaded.commands.PartyCmdManager;

public class SWRCommandImpl implements SWRCommandAPI {

    SkywarsReloadedAPI swrAPI;

    public SWRCommandImpl(SkywarsReloadedAPI swrAPIIn) {
        this.swrAPI = swrAPIIn;
    }

    @Override
    public MainCmdManager getMainCommandManager() {
        return null; //swrAPI.getSkywarsReloaded();
    }

    @Override
    public KitCmdManager getKitCommandManager() {
        return null;
    }

    @Override
    public MapCmdManager getMapCommandManager() {
        return null;
    }

    @Override
    public PartyCmdManager getPartyCommandManager() {
        return null;
    }
}
