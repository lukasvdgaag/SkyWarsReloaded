package net.gcnt.skywarsreloaded.game.chest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Manager to create, load and delete chest types from the plugin's chest folder.
 */
public interface ChestManager {

    /**
     * Load all the chest types in the chests folder of the plugin
     */
    void loadAllChestTypes();

    /**
     * Generate the normal and center chests if they don't already exist
     */
    void createDefaultsIfNotPresent();

    /**
     * Get a chest type by name
     * @param chestTypeName The name of the chest type
     * @return The {@link SWChestType} associated with the name provided or null if it doesn't exist
     */
    SWChestType getChestTypeByName(String chestTypeName);

    /**
     * Completely remove a chest type from the server
     * @param chestTypeName The name associated with the {@link SWChestType} to remove
     */
    void deleteChestType(String chestTypeName);

    /**
     * Get a list of chest types that are loaded from the chests folder
     * @return The list of chests
     */
    List<SWChestType> getChestTypes();

    /**
     * Create a new chest type and place it into the chests folder
     * @param chestTypeName The name of the chest type to create
     * @return The created {@link SWChestType}
     */
    SWChestType createChestType(@NotNull String chestTypeName);

    /**
     * Create an instance of the {@link SWChestType} class given the name provided.
     * @param chestTypeName The name of the chest type
     * @return The {@link SWChestType} associated with the name provided or null if it does not already exist
     *          in the chests folder
     */
    SWChestType initChestType(String chestTypeName);

}
