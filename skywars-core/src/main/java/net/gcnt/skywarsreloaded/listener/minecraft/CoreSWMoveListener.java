package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerMoveEvent;

public class CoreSWMoveListener {

    private final SkyWarsReloaded plugin;

    public CoreSWMoveListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerMoveEvent.class, this::onMove));
    }

    public void onMove(CoreSWPlayerMoveEvent event) {
        if (event.getPlayer().isFrozen()) event.setCancelled(true);
    }

}
