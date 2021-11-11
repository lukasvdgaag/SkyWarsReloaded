package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.game.kits.SWKit;

import java.util.List;

public interface ChestManager {

    void loadAllChestTypes();

    SWChestType getChestTypeByName(String kitId);

    void deleteChestType(String kitId);

    List<SWChestType> getChestTypes();

    SWChestType createChestType(String id);

}
