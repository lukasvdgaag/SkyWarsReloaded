package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;

import java.util.List;

public interface KitManager {

    void loadAllKits();

    SWKit getKitByName(String kitId);

    void deleteKit(String kitId);

    void createDefaultsIfNotPresent();

    List<SWKit> getKits();

    SWKit createKit(String id, Item icon);

    SWKit initKit(String id);

}
