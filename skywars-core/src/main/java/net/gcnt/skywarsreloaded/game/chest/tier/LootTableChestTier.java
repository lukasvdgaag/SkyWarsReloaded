package net.gcnt.skywarsreloaded.game.chest.tier;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.filler.SWChestFiller;
import net.gcnt.skywarsreloaded.game.types.ChestType;

public class LootTableChestTier extends AbstractSWChestTier {

    // todo implement loot table chest tier loader.
    public LootTableChestTier(SkyWarsReloaded plugin, String name) {
        super(plugin, name);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {

    }

    @Override
    public boolean hasChestType(ChestType chestType) {
        return false;
    }

    @Override
    public SWChestFiller getChestFiller() {
        return null;
    }
}
