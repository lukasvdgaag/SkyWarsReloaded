package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.concurrent.CompletableFuture;

public abstract class CoreLocalGameInstanceManager extends CoreGameInstanceManager<LocalGameInstance> implements LocalGameInstanceManager {

    public CoreLocalGameInstanceManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public LocalGameInstance getGameInstanceBySWWorld(SWWorld swWorld) {
        if (swWorld == null) return null;
        for (LocalGameInstance gameInstance : getGameInstancesList()) {
            if (gameInstance != null && gameInstance.getWorld().equals(swWorld)) return gameInstance;
        }
        return null;
    }

    @Override
    public CompletableFuture<Boolean> saveInstanceToTemplate(LocalGameInstance instance) {
        return plugin.getWorldLoader().save(instance);
    }

    @Override
    public CompletableFuture<Void> deleteGameInstance(LocalGameInstance world) {
        plugin.getWorldLoader().deleteWorldInstance(world);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean isManagerRemote() {
        return false;
    }
}
