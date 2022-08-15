package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWBlock;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWChest;

public class SimpleChestFiller implements SWChestFiller {

    @Override
    public void fillChest(SWChestTier chestTier, GameWorld world, SWCoord coord) {
        if (world == null || coord.getWorld() == null) return;
        SWBlock block = coord.getWorld().getBlockAt(coord.x(), coord.y(), coord.z());
        if (!(block instanceof SWChest)) return;

        SWChest chest = (SWChest) block;

        if (!block.getChunk().isLoaded()) {
            block.getChunk().load();
            return;
        }

        chest.clearContents();

        Item[] items = chestTier.generateChestLoot(world.getChestTier(), chest.getSize() > 27);
        chest.setContents(items);
    }

}
