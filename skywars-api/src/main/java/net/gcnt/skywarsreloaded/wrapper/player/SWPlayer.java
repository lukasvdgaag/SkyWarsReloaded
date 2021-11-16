package net.gcnt.skywarsreloaded.wrapper.player;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

/**
 * General data about a player that is independent of any running state of games or teams
 */
public interface SWPlayer extends SWCommandSender, SWOfflinePlayer {

    SWPlayerData getPlayerData();

    void setPlayerData(SWPlayerData playerData);

    Item getItemInHand(boolean offHand);

    Item[] getInventory();

    void setSlot(int slot, Item item);

    Item getSlot(int slot);

    Item getHelmet();

    void setHelmet(Item helmet);

    Item getChestplate();

    void setChestplate(Item chestplate);

    Item getLeggings();

    void setLeggings(Item leggings);

    Item getBoots();

    void setBoots(Item boots);

    void clearInventory();

}
