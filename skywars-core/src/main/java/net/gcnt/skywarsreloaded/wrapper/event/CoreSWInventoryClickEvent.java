package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

public class CoreSWInventoryClickEvent implements SWInventoryClickEvent {

    private final SWInventory inventory;
    private final SWGuiClickHandler.ClickType clickType;
    private final boolean isShiftClick;
    private final int slot;
    private final int rawSlot;
    private Item currentItem;
    private boolean cancelled;


    public CoreSWInventoryClickEvent(SWInventory inventory, SWGuiClickHandler.ClickType clickType, int slot, int rawSlot, boolean isShiftClick, Item currentItem) {
        this.inventory = inventory;
        this.clickType = clickType;
        this.slot = slot;
        this.isShiftClick = isShiftClick;
        this.rawSlot = rawSlot;
        this.currentItem = currentItem;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public SWInventory getInventory() {
        return this.inventory;
    }

    @Override
    public SWGuiClickHandler.ClickType getClick() {
        return this.clickType;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public int getRawSlot() {
        return this.rawSlot;
    }

    @Override
    public boolean isShiftClick() {
        return this.isShiftClick;
    }

    @Override
    public Item getCurrentItem() {
        return this.currentItem;
    }

    @Override
    public void setCurrentItem(Item item) {
        this.currentItem = item;
    }

}
