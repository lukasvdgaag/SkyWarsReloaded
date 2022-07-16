package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.game.types.GameDifficulty;
import net.gcnt.skywarsreloaded.utils.Item;

import java.util.HashMap;
import java.util.List;

public interface SWChestType {

    String getName();

    void loadData();

    void saveData();

    String getDisplayName();

    HashMap<GameDifficulty, HashMap<Integer, List<Item>>> getAllContents();

    HashMap<Integer, List<Item>> getContents(GameDifficulty difficulty);

    Item[] generateChestLoot(GameDifficulty difficulty, boolean doubleChest);

}
