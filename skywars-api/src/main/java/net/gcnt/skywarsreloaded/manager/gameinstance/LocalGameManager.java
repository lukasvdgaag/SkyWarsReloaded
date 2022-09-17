package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface LocalGameManager extends GameManager {

    /**
     * Get a {@link GameTemplate} by its wrapped world.
     *
     * @param swWorld The wrapped world object the game world is based on.
     * @return GameTemplate if found, null otherwise.
     */
    GameInstance getGameInstanceBySWWorld(SWWorld swWorld);

}
