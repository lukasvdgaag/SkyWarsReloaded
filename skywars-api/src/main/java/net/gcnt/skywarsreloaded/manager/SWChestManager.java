package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Manager to create, load and delete chest tiers from the plugin's chest folder.
 */
public interface SWChestManager {

    /**
     * Load all the chest tiers in the chests folder of the plugin
     */
    void loadAllChestTiers();

    /**
     * Generate the normal and center chests if they don't already exist
     */
    void createDefaultsIfNotPresent();

    /**
     * Get a chest tier by name
     *
     * @param chestTypeName The name of the chest tier
     * @return The {@link SWChestTier} associated with the name provided or null if it doesn't exist
     */
    SWChestTier getChestTierByName(String chestTypeName);

    /**
     * Completely remove a chest tier from the server
     *
     * @param chestTypeName The name associated with the {@link SWChestTier} to remove
     */
    void deleteChestTier(String chestTypeName);

    /**
     * Get a list of chest tiers that are loaded from the chests folder
     *
     * @return The list of chests
     */
    List<SWChestTier> getChestTiers();

    /**
     * Create a new chest tier and place it into the chests folder
     *
     * @param chestTypeName The name of the chest tier to create
     * @return The created {@link SWChestTier}
     */
    SWChestTier createChestTier(@NotNull String chestTypeName);

    /**
     * Create an instance of the {@link SWChestTier} class given the name provided.
     *
     * @param chestTypeName The name of the chest tier
     * @return The {@link SWChestTier} associated with the name provided or null if it does not already exist
     * in the chests folder
     */
    SWChestTier initChestTier(String chestTypeName);

    /**
     * Create an instance of the {@link SWChestTier} class given the name provided.
     * And create the chest tier file if it doesn't already exist.
     *
     * @param chestTypeName   The name of the chest tier
     * @param generateDefault If true, generate a default chest tier file if it doesn't already exist
     * @return The {@link SWChestTier} associated with the name provided or null if it does not already exist
     * in the chests folder
     */
    SWChestTier initChestTier(String chestTypeName, boolean generateDefault);

}
