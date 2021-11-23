package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;

public interface GameWorldLoader {

    /**
     * Create an instance of a minecraft world for the given game world based on the template associated
     * @param gameWorld The {@link GameWorld} used to determine data to be loaded
     * @return true if the template data already exists given the template name
     */
    boolean generateWorldInstance(GameWorld gameWorld) throws IllegalStateException, IllegalArgumentException ;

    void createEmptyWorld(GameWorld gameWorld);

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
    void deleteMap(GameTemplate gameTemplate);

    void createBasePlatform(GameWorld gameWorld);

    void updateWorldBorder(GameWorld gameWorld);

}
