package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;

import java.util.concurrent.CompletableFuture;

public interface GameWorldLoader {

    /**
     * Create an instance of a minecraft world for the given game world based on the template associated
     *
     * @param gameWorld The {@link GameWorld} used to determine data to be loaded
     * @return true if the template data already exists given the template name
     */
    CompletableFuture<Boolean> generateWorldInstance(GameWorld gameWorld) throws IllegalStateException, IllegalArgumentException;

    /**
     * Create an empty world for the corresponding GameWorld.
     *
     * @param gameWorld {@link GameWorld} to create a void world for.
     * @return {@link CompletableFuture<Void>} which completes when the world is finished creating.
     */
    CompletableFuture<Void> createEmptyWorld(GameWorld gameWorld);

    /**
     * Delete the world instance for the {@link GameWorld} provided
     *
     * @param gameWorld The {@link GameWorld} to delete the world for
     */
    void deleteWorldInstance(GameWorld gameWorld);

    /**
     * Delete the stored template map data for the given {@link GameTemplate}
     *
     * @param gameTemplate The {@link GameTemplate} to delete the map for
     */
    void deleteMap(GameTemplate gameTemplate, boolean forceUnloadInstances);

    /**
     * Create the base platform to spawn on when editing the map for the first time.
     *
     * @param gameWorld The {@link GameWorld} to create the platform in.
     */
    void createBasePlatform(GameWorld gameWorld);

    /**
     * Update the World Border for the target {@link GameWorld}.
     *
     * @param gameWorld {@link GameWorld} to update the border of.
     */
    void updateWorldBorder(GameWorld gameWorld);

    /**
     * Save the current world to disk for future use when creating instances of the map.
     */
    CompletableFuture<Boolean> save(GameWorld gameWorld);

}
