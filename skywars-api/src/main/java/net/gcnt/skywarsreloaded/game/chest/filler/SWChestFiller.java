package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWChestFiller {

    Item[] generateChestLoot(SWChestTier chestTier, ChestType chestType, boolean doubleChest);

    void fillChest(SWChestTier chestTier, LocalGameInstance world, SWCoord coord, ChestType chestType);

}
