package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;

import java.util.UUID;

/**
 * The game manager used when the plugin is set in proxy mode and this server is a lobby
 */
public interface RemoteGameInstanceManager extends GameInstanceManager<RemoteGameInstance> {

    void addCachedGameInstance(RemoteGameInstance instance);

    void removeCachedGameInstance(UUID instanceId);

    void removeCachedGameInstance(RemoteGameInstance instance);

    boolean isGameInstanceCached(UUID instanceId);

    RemoteGameInstance getCachedGameInstance(UUID instanceId);

    void updateCachedGameInstance(RemoteGameInstance instance);

}
