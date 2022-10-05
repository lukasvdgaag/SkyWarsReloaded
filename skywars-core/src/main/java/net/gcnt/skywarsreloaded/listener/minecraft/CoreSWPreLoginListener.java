package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWAsyncPlayerPreLoginEvent;

public class CoreSWPreLoginListener {

    private final SkyWarsReloaded plugin;

    public CoreSWPreLoginListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWAsyncPlayerPreLoginEvent.class, this::onPreLogin));
    }

    public void onPreLogin(CoreSWAsyncPlayerPreLoginEvent event) {
        SWPlayer player = this.plugin.getPlayerManager().initPlayer(event.getUUID());
        this.plugin.getPlayerStorage().loadData(player);
    }

}
