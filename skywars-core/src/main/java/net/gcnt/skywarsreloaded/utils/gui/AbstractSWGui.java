package net.gcnt.skywarsreloaded.utils.gui;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

import java.util.HashMap;

public abstract class AbstractSWGui implements SWGui {

    protected SkyWarsReloaded plugin;
    protected SWInventory inventory;
    protected String title;
    protected int size;

    protected HashMap<Integer, SWGuiClickHandler> clickHandlers;
    protected SWPlayer player;

    public AbstractSWGui(SkyWarsReloaded plugin, String title, int size, SWPlayer player) {
        this.plugin = plugin;
        this.title = title;
        this.size = size;
        this.clickHandlers = new HashMap<>();
        this.player = player;
        this.inventory = null;
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
            this.plugin.getLogger().error(String.format("Attempted to add a button to an already existing slot %d", slot));
            return this;
        }

        this.clickHandlers.put(slot, handler);
        this.inventory.setItem(slot, item);
        return this;
    }

    @Override
    public void removeButton(int slot) {
        this.clickHandlers.remove(slot);
        this.inventory.setItem(slot, null);
    }

    @Override
    public void updateButton(int slot, Item item) {
        this.inventory.setItem(slot, item);
    }

    @Override
    public void open() {
        this.inventory = plugin.getServer().createInventory(title, size);
        this.plugin.getGuiManager().registerInventoryCreation(this);
        player.openInventory(this.inventory);
    }

    @Override
    public void close() {
        if (isViewing()) player.closeInventory();
        if (this.inventory != null) this.plugin.getGuiManager().unregisterInventory(this.inventory);
        this.inventory = null;
    }

    @Override
    public boolean isViewing() {
        return this.inventory.isViewing(player);
    }

    @Override
    public SWPlayer getPlayer() {
        return this.player;
    }

    @Override
    public SWInventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(int slot, SWGuiClickHandler.ClickType clickType, boolean isShiftClick) {
        final SWGuiClickHandler handler = this.clickHandlers.get(slot);
        if (handler != null) handler.onClick(this, slot, clickType, isShiftClick);
    }

    // Platform handlers

    private void refreshInventory() {
        if (this.isViewing()) {
            this.close();
            this.open();
        }
    }
}
