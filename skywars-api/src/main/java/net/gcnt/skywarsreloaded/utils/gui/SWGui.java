package net.gcnt.skywarsreloaded.utils.gui;

import net.gcnt.skywarsreloaded.utils.Item;

public interface SWGui {

    String getTitle();

    void setTitle(String title);

    int getSize();

    void setSize(int size);

    void open();

    void close();

    boolean isViewing();

    SWGui addButton(int slot, Item item, SWGuiClickHandler handler);

    void updateButton(int slot, Item item);

    void removeButton(int slot);

}
