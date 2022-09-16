package net.gcnt.skywarsreloaded.wrapper.server;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;

public interface SWInventory {

    void clear();

    void setItem(int slot, Item item);

    Item getItem(int slot);

    Item[] getContents();

    void setContents(Item[] items);

    String getTitle();

    int getSize();

    List<SWPlayer> getViewers();

    boolean isViewing(SWPlayer player);

}
