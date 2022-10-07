package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWInventoryClickEvent;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

public class CoreSWInventoryClickListener {

    private final SkyWarsReloaded plugin;

    public CoreSWInventoryClickListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWInventoryClickEvent.class, this::onClick));
    }

    public void onClick(CoreSWInventoryClickEvent event) {
        CoreSWEventListener.cancelWhenWaitingInGame(event);

        SWInventory inv = event.getInventory();
        SWGui gui = this.plugin.getGuiManager().getActiveGui(inv);
        if (gui == null) return;

        event.setCancelled(true);

        gui.handleClick(event.getSlot(), event.getClick(), event.isShiftClick());
    }


}
