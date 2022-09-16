package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

public interface SWInventoryManager {

    void registerInventory(SWInventory inventory);

    void unregisterInventory(SWInventory inventory);

    void replaceInventory(SWInventory oldInventory, SWInventory newInventory);

}
