package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;

import java.util.UUID;

/**
 * The game manager used when the plugin is set in proxy mode and this server is a lobby
 */
public interface RemoteGameInstanceManager extends GameInstanceManager {

    void addCachedGameInstance(GameInstance instance);

    void removeCachedGameInstance(UUID instanceId);

    void removeCachedGameInstance(GameInstance instance);

    void updateCachedGameInstance(UUID instanceId, GameInstance instance);

}
