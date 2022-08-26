package net.gcnt.skywarsreloaded.wrapper.world.block;

import net.gcnt.skywarsreloaded.utils.Item;

public interface SWChest extends SWBlock {

    // Getters

    Item[] getContents();

    int getSize();

    // Setters

    void setContents(Item[] items);

    void setItem(int slot, Item item);

    // Actions

    void clearContents();

}
