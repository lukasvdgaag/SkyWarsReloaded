package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;

public class LootTableChestFiller extends AbstractSWChestFiller {

    public LootTableChestFiller(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public Item[] generateChestLoot(SWChestTier chestTier, ChestType chestType, boolean doubleChest) {
        return new Item[0];
    }

}
