package net.gcnt.skywarsreloaded.utils.gui;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.HashMap;

public abstract class AbstractSWGui implements SWGui {

    protected String title;
    protected int size;

    protected HashMap<Integer, SWGuiClickHandler> clickHandlers;
    protected SWPlayer viewer;

    public AbstractSWGui(String title, int size, SWPlayer player) {
        this.title = title;
        this.size = size;
        this.clickHandlers = new HashMap<>();
        this.viewer = player;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        if (this.isViewing()) {
            this.refreshInventory();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
        if (this.isViewing()) {
            this.refreshInventory();
        }
    }

    /**
     * Remember to call super!
     *
     * @param slot    the slot
     * @param item    the item
     * @param handler the handler
     * @return
     */
    @Override
    public SWGui addButton(int slot, Item item, SWGuiClickHandler handler) {
        if (this.clickHandlers.containsKey(slot)) {
            // fail
            return this;
        }

        this.clickHandlers.put(slot, handler);
        return this;
    }

    @Override
    public void removeButton(int slot) {
        this.clickHandlers.remove(slot);
    }

    // Platform handlers

    public abstract void refreshInventory();

}
