package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractChestManager implements ChestManager {

    public final SkyWarsReloaded plugin;
    public HashMap<String, SWChestType> chests;

    public AbstractChestManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.chests = new HashMap<>();
    }

    @Override
    public abstract void loadAllChestTypes();

    @Override
    public SWChestType getChestTypeByName(String chestId) {
        return chests.getOrDefault(chestId, null);
    }

    @Override
    public void deleteChestType(String chestId) {
        // todo chest type deletion here
    }

    @Override
    public List<SWChestType> getChestTypes() {
        return chests.values().stream().toList();
    }

    @Override
    public abstract SWChestType createChestType(String id);

}
