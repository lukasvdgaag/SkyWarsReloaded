package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerFoodLevelChangeEvent;

public class CoreSWFoodLevelChangeListener {

    private final SkyWarsReloaded plugin;

    public CoreSWFoodLevelChangeListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerFoodLevelChangeEvent.class, this::onFoodLevelChange));
    }

    public void onFoodLevelChange(CoreSWPlayerFoodLevelChangeEvent event) {
        if (CoreSWEventListener.cancelWhenWaitingInGame(event)) {
            event.setFoodLevel(20);
        }
    }

}
