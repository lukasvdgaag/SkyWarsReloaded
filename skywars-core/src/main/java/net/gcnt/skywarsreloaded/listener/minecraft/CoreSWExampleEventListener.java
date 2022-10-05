package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWBlockBreakEvent;

public class CoreSWExampleEventListener {

    public CoreSWExampleEventListener(SkyWarsReloaded plugin) {
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWBlockBreakEvent.class, this::handleBreak));
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWBlockBreakEvent.class, (event) -> {
            event.getBlockTypeName();
        }));

    }

    private void handleBreak(CoreSWBlockBreakEvent event) {
    }

}
