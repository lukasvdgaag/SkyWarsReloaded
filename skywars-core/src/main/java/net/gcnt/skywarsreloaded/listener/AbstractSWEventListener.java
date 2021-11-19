package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.event.*;

public class AbstractSWEventListener implements SWEventListener {

    public final SkyWarsReloaded plugin;

    public AbstractSWEventListener(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
    }

    @Override
    public void onAsyncPlayerPreLogin(SWAsyncPlayerPreLoginEvent event) {

    }

    @Override
    public void onPlayerJoin(SWPlayerJoinEvent event) {

    }

    @Override
    public void onPlayerQuit(SWPlayerQuitEvent event) {
        this.plugin.getPlayerManager().removePlayer(event.getPlayer());
    }

    @Override
    public void onPlayerInteract(SWPlayerInteractEvent event) {

    }

    @Override
    public void onPlayerBlockBreak(SWBlockBreakEvent event) {

    }

    @Override
    public void onPlayerBlockPlace(SWBlockPlaceEvent event) {

    }

}
