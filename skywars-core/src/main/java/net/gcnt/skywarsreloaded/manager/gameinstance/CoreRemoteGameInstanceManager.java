package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CoreRemoteGameInstanceManager extends CoreGameInstanceManager<RemoteGameInstance> implements RemoteGameInstanceManager {

    public CoreRemoteGameInstanceManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<Boolean> saveInstanceToTemplate(RemoteGameInstance instance) {
        final CompletableFuture<Boolean> booleanCompletableFuture = new CompletableFuture<>();
        booleanCompletableFuture.complete(true);
        return booleanCompletableFuture; // todo: request save to messaging handler (redis or sql)
    }

    @Override
    public CompletableFuture<RemoteGameInstance> createGameWorld(GameTemplate data) {
        return null; // todo
    }

    @Override
    public void addCachedGameInstance(RemoteGameInstance instance) {
        this.getGameInstances().put(instance.getId(), instance);
    }

    @Override
    public void removeCachedGameInstance(UUID instanceId) {
        this.getGameInstances().remove(instanceId);
    }

    @Override
    public void removeCachedGameInstance(RemoteGameInstance instance) {
        this.removeCachedGameInstance(instance.getId());
    }

    @Override
    public boolean isGameInstanceCached(UUID instanceId) {
        return this.getGameInstances().containsKey(instanceId);
    }

    @Override
    public RemoteGameInstance getCachedGameInstance(UUID instanceId) {
        return this.getGameInstances().get(instanceId);
    }

    @Override
    public void updateCachedGameInstance(RemoteGameInstance instance) {
        this.getGameInstances().put(instance.getId(), instance);
    }

    @Override
    public boolean isManagerRemote() {
        return true;
    }
}
