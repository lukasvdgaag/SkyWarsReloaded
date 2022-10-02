package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

public interface SWGuiManager {

    SWGui createJoinTypeSelectorGui(SWPlayer player);

    SWGui createJoinGameGui(SWPlayer player);

    SWGui createOptionsGui(SWPlayer player);

    SWGui createVotingGui(SWPlayer player);

    SWGui createKitGui(SWPlayer player);

    void registerInventoryCreation(SWGui gui);

    void unregisterInventory(SWInventory inventory);

    void unregisterGui(SWGui gui);

    SWGui getActiveGui(SWInventory inventory);

}
