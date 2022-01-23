package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.game.types.GameDifficulty;
import net.gcnt.skywarsreloaded.utils.Item;

import java.util.HashMap;

public interface SWChestType {

    String getName();

    void loadData();

    void saveData();

    String getDisplayName();

    HashMap<GameDifficulty, HashMap<Integer, Item>> getAllContents();

}
