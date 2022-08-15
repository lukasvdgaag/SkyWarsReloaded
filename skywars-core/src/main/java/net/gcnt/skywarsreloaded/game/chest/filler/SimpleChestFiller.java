package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWBlock;
import net.gcnt.skywarsreloaded.wrapper.world.block.SWChest;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleChestFiller implements SWChestFiller {

    private static final List<Integer> RANDOM_SLOTS;
    private static final List<Integer> RANDOM_SLOTS_DOUBLE;
    private static final Random RANDOM;

    static {
        RANDOM_SLOTS = new ArrayList<>();
        RANDOM_SLOTS_DOUBLE = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            RANDOM_SLOTS_DOUBLE.add(i);
            if (i < 27) RANDOM_SLOTS.add(i);
        }

        RANDOM = ThreadLocalRandom.current();
    }

    private final SkyWarsReloaded plugin;

    public SimpleChestFiller(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public Item[] generateChestLoot(SWChestTier chestTier, ChestType chestType, boolean doubleChest) {
        // hashmap with a list of items per chance of occurrence
        int maxItems = plugin.getConfig().getInt(doubleChest ? ConfigProperties.GAME_CHESTS_MAX_ITEMS_DOUBLE.toString() : ConfigProperties.GAME_CHESTS_MAX_ITEMS.toString());

        List<Integer> slots = doubleChest ? RANDOM_SLOTS_DOUBLE : RANDOM_SLOTS;
        Collections.shuffle(slots);

        HashMap<Integer, List<Item>> items = getContents(type);
        Item[] loot = new Item[slots.size()];
        if (items == null) return loot;

        int added = 0;
        adding:
        for (int chance : items.keySet()) {
            for (Item item : items.get(chance)) {
                if (item == null) continue;

                if (RANDOM.nextInt(100) + 1 <= chance) {
                    loot[slots.get(added)] = item;
                    added++;
                    if (added >= maxItems || added >= slots.size() - 1) break adding;
                }
            }
        }

        return loot;
    }

    @Override
    public void fillChest(SWChestTier chestTier, GameWorld world, SWCoord coord, ChestType chestType) {
        if (world == null || coord.getWorld() == null) return;
        SWBlock block = coord.getWorld().getBlockAt(coord.x(), coord.y(), coord.z());
        if (!(block instanceof SWChest)) return;

        SWChest chest = (SWChest) block;

        if (!block.getChunk().isLoaded()) {
            block.getChunk().load();
            return;
        }

        chest.clearContents();

        Item[] items = chestTier.generateChestLoot(chestType, chest.getSize() > 27);
        chest.setContents(items);
    }

}
