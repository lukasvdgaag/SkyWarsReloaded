package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.Item;

import java.util.List;

public interface SWPlayerDeathEvent extends SWPlayerEvent {

    String getDeathMessage();

    void setDeathMessage(String message);

    boolean isKeepInventory();

    void setKeepInventory(boolean keepInventory);

    List<Item> getDrops();

}
