package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWChestFiller {

    void fillChest(SWChestTier chestType, GameWorld world, SWCoord coord);

}
