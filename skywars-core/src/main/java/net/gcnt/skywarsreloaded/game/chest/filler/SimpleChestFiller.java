package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.chest.tier.SimpleChestTier;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SimpleChestFiller extends AbstractSWChestFiller {

    private static final List<Integer> RANDOM_SLOTS;
    private static final List<Integer> RANDOM_SLOTS_DOUBLE;

    static {
        RANDOM_SLOTS = new ArrayList<>();
        RANDOM_SLOTS_DOUBLE = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            RANDOM_SLOTS_DOUBLE.add(i);
            if (i < 27) RANDOM_SLOTS.add(i);
        }
    }

    public SimpleChestFiller(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public Item[] generateChestLoot(SWChestTier chestTier, ChestType chestType, boolean doubleChest) {
        if (!(chestTier instanceof SimpleChestTier)) {
            plugin.getLogger().error("SkyWarsReloaded is trying to fill a chest with a chest tier using simplified structure that is not a SimpleChestTier (tier: " + chestTier.getName() + ")!");
            return new Item[]{};
        }
        SimpleChestTier simpleChestTier = (SimpleChestTier) chestTier;

        // hashmap with a list of items per chance of occurrence
        int maxItems = plugin.getConfig().getInt(doubleChest ? ConfigProperties.GAME_CHESTS_MAX_ITEMS_DOUBLE.toString() : ConfigProperties.GAME_CHESTS_MAX_ITEMS.toString());

        List<Integer> slots = doubleChest ? RANDOM_SLOTS_DOUBLE : RANDOM_SLOTS;
        Collections.shuffle(slots);

        HashMap<Integer, List<Item>> items = simpleChestTier.getContents(chestType);
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

}
