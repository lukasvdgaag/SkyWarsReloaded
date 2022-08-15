package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;

import java.util.HashMap;
import java.util.List;

public interface SWChestTier {

    String getName();

    void loadData();

    void saveData();

    String getDisplayName();

    Item[] generateChestLoot(ChestType difficulty, boolean doubleChest);\ // todo move to filler

    String getMode();
}
