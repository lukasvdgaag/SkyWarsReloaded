package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameInstance;

import java.util.concurrent.CompletableFuture;

public abstract class CoreRemoteGameInstanceManager extends CoreGameInstanceManager implements RemoteGameManager {

    public CoreRemoteGameInstanceManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<Boolean> saveInstanceToTemplate(GameInstance instance) {
        return plugin.getWorldLoader().save(instance); // todo: request save to messaging handler (redis or sql)
    }

}
