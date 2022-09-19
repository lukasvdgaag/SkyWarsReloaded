package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

/**
 * Manager used when the plugin is in "not-proxy" mode
 */
public interface LocalGameInstanceManager extends GameInstanceManager<LocalGameInstance> {

    /**
     * Get a {@link GameTemplate} by its wrapped world.
     *
     * @param swWorld The wrapped world object the game world is based on.
     * @return GameTemplate if found, null otherwise.
     */
    GameInstance getGameInstanceBySWWorld(SWWorld swWorld);

}
