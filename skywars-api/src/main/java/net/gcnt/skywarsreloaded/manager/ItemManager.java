package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.utils.Item;

import java.util.Map;

public interface ItemManager {

    void loadDefaultItems();

    Item createItem(String material);

    Item createItem(String material, int amount);

    Item getItem(Map<?, ?> map);

    Item getItemFromConfig(String path);


}
