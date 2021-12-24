package net.gcnt.skywarsreloaded.wrapper.world;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.List;

public interface SWWorld {

    /**
     * Get all the players that are currently in this world
     *
     * @return A list of the players in this world
     */
    List<SWPlayer> getAllPlayers();

    /**
     * Set the block at a location to a block type
     *
     * @param location The location of where to set the spawn
     * @param block The block to set at that location
     */
    void setBlockAt(SWCoord location, Item block);

    /**
     * Set the block at a location to a block type
     *
     * @param location The location of where to set the spawn
     * @param blockName The name of the block to set at that location
     */
    void setBlockAt(SWCoord location, String blockName);

    /**
     * Get the world's name
     *
     * @return The world's name
     */
    String getName();

    /**
     * Get the world's default spawn location
     *
     * @return {@link SWCoord} of the world's default spawn location
     */
    SWCoord getDefaultSpawnLocation();

    /**
     * Unload the wrapped world from the server
     *
     * @param saveChunks Whether to save the chunks before unloading
     */
    void unload(boolean saveChunks);

    /**
     * Get if the world that is wrapped is loaded / exists
     *
     * @return If the world is loaded
     */
    boolean isLoaded();

    /**
     * Set whether spawn chunks should automatically be always loaded
     *
     * @param keepSpawnLoaded true if spawn chunk should always be loaded.
     */
    void setKeepSpawnLoaded(boolean keepSpawnLoaded);
}
