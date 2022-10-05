package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerQuitEvent;

public class CoreSWQuitListener {

    private final SkyWarsReloaded plugin;

    public CoreSWQuitListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerQuitEvent.class, this::onQuit));
    }

    public void onQuit(CoreSWPlayerQuitEvent event) {
        this.plugin.getPlayerManager().removePlayer(event.getPlayer());
    }

}
