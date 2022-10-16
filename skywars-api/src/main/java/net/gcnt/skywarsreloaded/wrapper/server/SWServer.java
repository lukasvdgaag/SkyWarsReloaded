package net.gcnt.skywarsreloaded.wrapper.server;

import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.UUID;

public interface SWServer {

    /**
     * Get the wrapper to the default world. Minecraft is required to have this loaded at all times.
     *
     * @return The default world
     */
    SWWorld getDefaultWorld();

    int getPlayerCount();

    /**
     * Get a world by its name
     *
     * @return The world with the name provided
     */
    SWWorld getWorld(String name);

    /**
     * Register a world into the wrapper world cache
     *
     * @param world The world to register
     */
    void registerWorld(UUID serverWorldUUID, SWWorld world);

    /**
     * Create a new Inventory.
     *
     * @param title The title of the inventory
     * @param size  The size of the inventory
     * @return The new inventory
     */
    SWInventory createInventory(String title, int size);

    /**
     * Create a new inventory with a specific inventory type.
     *
     * @param title         The title of the inventory
     * @param inventoryType The type of inventory
     * @return The new inventory
     */
    SWInventory createInventory(String title, String inventoryType);

    boolean isPluginEnabled(String plugin);

}
