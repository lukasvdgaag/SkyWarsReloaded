package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.game.GameWorld;

public interface GameWorldLoader {

    void generateWorld(GameWorld gameWorld);

    void createEmptyWorld(GameWorld gameWorld);

    void loadSchematic(GameWorld gameWorld);

    void deleteWorld(GameWorld gameWorld);

}
