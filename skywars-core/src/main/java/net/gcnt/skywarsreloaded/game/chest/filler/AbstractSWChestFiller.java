package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
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
    public void fillChest(SWChestTier chestTier, GameWorld gameWorld, SWCoord coord, ChestType chestType) {
        System.out.println("FILLING CHEST!");
        if (gameWorld == null) {
            System.out.println("gameWorld is null");
            return;
        }

        SWWorld world = gameWorld.getWorld();
        SWBlock block = world.getBlockAt(coord.x(), coord.y(), coord.z());
        if (!(block instanceof SWChest)) {
            System.out.println("block is not a chest");
            return;
        }

        SWChest chest = (SWChest) block;

        if (!block.getChunk().isLoaded()) {
            System.out.println("WAS NOT LOADED!");
            block.getChunk().load();
        }
        System.out.println("IS LOADED!");

        chest.clearContents();

        System.out.println("PRE GEN!");
        Item[] items = generateChestLoot(chestTier, chestType, chest.getSize() > 27);
        System.out.println("POST GEN!");
        chest.setContents(items);
    }

}
