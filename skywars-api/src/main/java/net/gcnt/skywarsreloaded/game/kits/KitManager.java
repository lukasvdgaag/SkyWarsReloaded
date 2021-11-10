package net.gcnt.skywarsreloaded.game.kits;

import java.util.List;

public interface KitManager {

    void loadAllKits();

    SWKit getKitByName(String kitId);

    void deleteKit(String kitId);

    List<SWKit> getKits();

    SWKit createKit(String id);

}
