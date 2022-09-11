package net.gcnt.skywarsreloaded.game.chest.filler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.manager.SWChestFillerManager;

import java.util.HashMap;

public class ChestFillerManager implements SWChestFillerManager {

    public static final String SIMPLE = "simple";
    public static final String LOOT_TABLE = "loottable";

    private final HashMap<Class<? extends SWChestFiller>, SWChestFiller> chestFillers;

    public ChestFillerManager(SkyWarsReloaded plugin) {
        this.chestFillers = new HashMap<>();
        this.chestFillers.put(SimpleChestFiller.class, new SimpleChestFiller(plugin));
        this.chestFillers.put(LootTableChestFiller.class, new LootTableChestFiller(plugin));
    }

    public SWChestFiller getFillerByName(String name) {
        if (name.equalsIgnoreCase(SIMPLE)) {
            return chestFillers.get(SimpleChestFiller.class);
        } else if (name.equalsIgnoreCase(LOOT_TABLE)) {
            return chestFillers.get(LootTableChestFiller.class);
        } else {
            throw new IllegalArgumentException("Requested chest filler which doesn't exist");
        }
    }

}
