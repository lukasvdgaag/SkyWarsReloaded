package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.HashMap;
import java.util.List;

public interface SWKit {

    String getId();

    void loadData();

    void saveData();

    String getDisplayName();

    void setDisplayName(String name);

    Item getIcon();

    void setIcon(Item icon);

    Item getUnavailableIcon();

    void setUnavailableIcon(Item icon);

    int getSlot();

    void setSlot(int slot);

    String getDescription();

    void setDescription(String description);

    List<String> getLore();

    void setLore(List<String> lore);

    String getPermission();

    Item getHelmet();

    void setHelmet(Item item);

    Item getChestplate();

    void setChestplate(Item item);

    Item getLeggings();

    void setLeggings(Item item);

    Item getBoots();

    void setBoots(Item item);

    HashMap<Integer, Item> getContents();

    void setContents(HashMap<Integer, Item> contents);

    Item getOffHand();

    void setOffHand(Item item);

    List<String> getEffects();

    void setEffects(List<String> effects);

    void giveToPlayer(SWPlayer sp);

    void updateItems();

}
