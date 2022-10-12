package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWBlock;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWChest;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSWChestFiller implements SWChestFiller {

    protected static final Random RANDOM;

    static {
        RANDOM = ThreadLocalRandom.current();
    }

    protected final SkyWarsReloaded plugin;

    public AbstractSWChestFiller(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public void fillChest(SWChestTier chestTier, LocalGameInstance gameInstance, SWCoord coord, ChestType chestType) {
        if (gameInstance == null) {
            return;
        }

        SWWorld world = gameInstance.getWorld();
        SWBlock block = world.getBlockAt(coord.x(), coord.y(), coord.z());
        if (!(block instanceof SWChest)) {
            return;
        }

        SWChest chest = (SWChest) block;

        if (!block.getChunk().isLoaded()) {
            block.getChunk().load();
        }

        chest.clearContents();

        Item[] items = generateChestLoot(chestTier, chestType, chest.getSize() > 27);
        chest.setContents(items);
    }

}
