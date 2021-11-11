package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.game.GameType;
import net.gcnt.skywarsreloaded.utils.Item;

import java.util.HashMap;

public interface SWChestType {

    String getName();

    void loadData();

    void saveData();

    String getDisplayName();

    HashMap<GameType, HashMap<Integer, Item>> getAllContents();

}
