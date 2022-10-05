package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerInteractEvent;
import net.gcnt.skywarsreloaded.wrapper.event.SWPlayerInteractEvent;

public class CoreSWInteractionListener {

    private final SkyWarsReloaded plugin;

    public CoreSWInteractionListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerInteractEvent.class, this::onInteract));
    }

    public void onInteract(CoreSWPlayerInteractEvent event) {
        if (event.getClickedBlockType().toLowerCase().contains("chest")) {
            if (event.getAction() == SWPlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                event.getPlayer().sendMessage("opening chest.");
                plugin.getNMSManager().getNMS().setChestOpen(event.getClickedBlockLocation(), true);
            }
        }
    }

}
