package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CoreRemoteGameInstanceManager extends CoreGameInstanceManager implements RemoteGameInstanceManager {


    public CoreRemoteGameInstanceManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<Boolean> saveInstanceToTemplate(GameInstance instance) {
        return plugin.getWorldLoader().save(instance); // todo: request save to messaging handler (redis or sql)
    }

    @Override
    public CompletableFuture<GameInstance> createGameWorld(GameTemplate data) {
        return null; // todo
    }

    @Override
    public void addCachedGameInstance(GameInstance instance) {
        this.getGameInstances().put(instance.getId(), instance);
    }

    @Override
    public void removeCachedGameInstance(UUID instanceId) {
        this.getGameInstances().remove(instanceId);
    }

    @Override
    public void removeCachedGameInstance(GameInstance instance) {
        this.removeCachedGameInstance(instance.getId());
    }

    @Override
    public void updateCachedGameInstance(UUID instanceId, GameInstance instance) {
        this.getGameInstances().put(instanceId, instance);
    }
}
