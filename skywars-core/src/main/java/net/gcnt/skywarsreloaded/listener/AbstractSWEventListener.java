package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.SWOfflinePlayer;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public class AbstractSWEventListener implements SWEventListener {

    public final SkyWarsReloaded plugin;

    public AbstractSWEventListener(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
    }

    @Override
    public void onAsyncPlayerPreLogin(SWOfflinePlayer player) {

    }

    @Override
    public void onPlayerJoin(SWPlayer player) {

    }

    @Override
    public void onPlayerQuit(SWPlayer player) {
        this.plugin.getPlayerManager().removePlayer(player);
    }

    @Override
    public void onPlayerInteract(SWPlayer player) {

    }

    @Override
    public void onPlayerBlockBreak(SWPlayer player) {

    }

    @Override
    public void onPlayerBlockPlace(SWPlayer player) {

    }

}
