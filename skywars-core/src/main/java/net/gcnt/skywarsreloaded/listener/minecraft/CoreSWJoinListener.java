package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerJoinEvent;

public class CoreSWJoinListener {

    private final SkyWarsReloaded plugin;

    public CoreSWJoinListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerJoinEvent.class, this::onJoin));
    }

    public void onJoin(CoreSWPlayerJoinEvent event) {
        event.getPlayer().fetchParentPlayer();
    }

}
