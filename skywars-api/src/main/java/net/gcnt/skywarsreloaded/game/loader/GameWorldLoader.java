package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.game.GameWorld;

public interface GameWorldLoader {

    /**
     * Create an instance of a minecraft world for the given game world based on the template associated
     * @param gameWorld The {@link GameWorld} used to determine data to be loaded
     * @return true if the template data already exists give the template name
     */
    boolean generateWorldInstance(GameWorld gameWorld);

    void createEmptyWorld(GameWorld gameWorld);

    void deleteWorld(GameWorld gameWorld);

    void createBasePlatform(GameWorld gameWorld);

    void updateWorldBorder(GameWorld gameWorld);

}
