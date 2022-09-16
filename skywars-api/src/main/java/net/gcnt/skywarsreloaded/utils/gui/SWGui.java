package net.gcnt.skywarsreloaded.utils.gui;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

public interface SWGui {

    /**
     * Get the title of the GUI.
     *
     * @return The title
     */
    String getTitle();

    /**
     * Set the title of the GUI.
     *
     * @param title The title
     */
    void setTitle(String title);

    /**
     * Get the size of the GUI (in rows)
     *
     * @return Number of rows
     */
    int getSize();

    /**
     * Set the size of the GUI (in rows)
     *
     * @param size Number of rows
     */
    void setSize(int size);

    /**
     * Open the GUI for the player assigned to this GUI instance
     */
    void open();

    /**
     * Close the GUI for the player assigned to this GUI instance
     */
    void close();

    /**
     * Get if the assigned player is currently viewing the GUI
     *
     * @return If the player is viewing the GUI
     */
    boolean isViewing();

    /**
     * Adds a clickable button to the GUI
     *
     * @param slot    The slot index of the GUI (mapped to inventory indexes)
     * @param item    The {@link Item} to use as the button icon. Use the {@link net.gcnt.skywarsreloaded.manager.ItemManager} to create them
     * @param handler The {@link SWGuiClickHandler} which will be called on click
     * @return The same
     */
    SWGui addButton(int slot, Item item, SWGuiClickHandler handler);

    void updateButton(int slot, Item item);

    void removeButton(int slot);

    SWPlayer getPlayer();

    SWInventory getInventory();

    void handleClick(int slot, SWGuiClickHandler.ClickType clickType, boolean isShiftClick);
}
