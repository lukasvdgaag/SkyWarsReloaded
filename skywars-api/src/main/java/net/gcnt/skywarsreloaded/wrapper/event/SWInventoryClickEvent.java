package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

public interface SWInventoryClickEvent extends SWEvent, SWCancellable, SWPlayerEvent {

    SWInventory getInventory();

    SWGuiClickHandler.ClickType getClick();

    int getSlot();

    int getRawSlot();

    boolean isShiftClick();

    Item getCurrentItem();

    void setCurrentItem(Item item);

}
