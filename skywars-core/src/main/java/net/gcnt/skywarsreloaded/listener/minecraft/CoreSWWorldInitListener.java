package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWWorldInitEvent;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public class CoreSWWorldInitListener {

    private final SkyWarsReloaded plugin;

    public CoreSWWorldInitListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWWorldInitEvent.class, this::onInit));
    }

    public void onInit(CoreSWWorldInitEvent event) {
        SWWorld world = event.getWorld();
        if (this.plugin.getGameInstanceManager().getGameTemplateByName(world.getName()) != null) {
            world.setKeepSpawnLoaded(false);
        }
    }

}
