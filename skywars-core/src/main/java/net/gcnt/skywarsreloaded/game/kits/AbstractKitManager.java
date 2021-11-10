package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractKitManager implements KitManager {

    public final SkyWarsReloaded plugin;
    public HashMap<String, SWKit> kits;

    public AbstractKitManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.kits = new HashMap<>();
    }

    @Override
    public abstract void loadAllKits();

    @Override
    public SWKit getKitByName(String kitId) {
        return kits.getOrDefault(kitId, null);
    }

    @Override
    public void deleteKit(String kitId) {
        // todo kit deletion here
    }

    @Override
    public List<SWKit> getKits() {
        return kits.values().stream().toList();
    }

    @Override
    public abstract SWKit createKit(String id);

}
