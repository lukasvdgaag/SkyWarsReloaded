package net.gcnt.skywarsreloaded.game.chest.filler;

import java.util.HashMap;

public class ChestFillerManager implements SWChestFillerManager {

    private final HashMap<Class<? extends SWChestFiller>, SWChestFiller> chestFillers;

    public ChestFillerManager() {
        this.chestFillers = new HashMap<>();
        this.chestFillers.put(SimpleChestFiller.class, new SimpleChestFiller());
        this.chestFillers.put(LootTableChestFiller.class, new LootTableChestFiller());
    }

    public SWChestFiller getFillerByName(String name) {
        if (name.equalsIgnoreCase("simple")) {
            return chestFillers.get(SimpleChestFiller.class);
        } else if (name.equalsIgnoreCase("loottable")) {
            return chestFillers.get(LootTableChestFiller.class);
        } else {
            throw new IllegalArgumentException("Requested chest filler which doesn't exist");
        }
    }

}
