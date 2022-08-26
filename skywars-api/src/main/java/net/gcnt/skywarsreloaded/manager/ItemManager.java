package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.utils.Item;

import java.util.Map;

public interface ItemManager {

    Item createItem(String material);

    Item createItem(String material, int amount);

    Item getItem(Map<?, ?> map);


}
