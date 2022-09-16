package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.gui.join.CoreJoinGameGui;
import net.gcnt.skywarsreloaded.gui.join.CoreJoinTypeSelectorGui;
import net.gcnt.skywarsreloaded.gui.options.CoreOptionsGui;
import net.gcnt.skywarsreloaded.gui.voting.CoreVotingGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWInventory;

import java.util.HashMap;

public class CoreGuiManager implements SWGuiManager {

    private final SkyWarsReloaded plugin;
    private final HashMap<SWInventory, SWGui> guiMap;

    public CoreGuiManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.guiMap = new HashMap<>();
    }

    // Solo vs Normal
    @Override
    public SWGui createJoinTypeSelectorGui(SWPlayer player) {
        return new CoreJoinTypeSelectorGui(plugin, player);
    }

    // Pick a running game
    @Override
    public SWGui createJoinGameGui(SWPlayer player) {
        return new CoreJoinGameGui(plugin, player);
    }

    // Root options gui
    @Override
    public SWGui createOptionsGui(SWPlayer player) {
        return new CoreOptionsGui(plugin, player);
    }

    // Root voting gui
    @Override
    public SWGui createVotingGui(SWPlayer player) {
        return new CoreVotingGui(plugin, player);
    }

    public void registerInventoryCreation(SWGui gui) {
        this.guiMap.put(gui.getInventory(), gui);
        plugin.getInventoryManager().registerInventory(gui.getInventory());
    }

    public void unregisterInventory(SWInventory inventory) {
        this.guiMap.remove(inventory);
        plugin.getInventoryManager().unregisterInventory(inventory);
    }

    public void unregisterGui(SWGui gui) {
        this.guiMap.remove(gui.getInventory());
    }

    public SWGui getActiveGui(SWInventory inv) {
        return this.guiMap.get(inv);
    }

}
