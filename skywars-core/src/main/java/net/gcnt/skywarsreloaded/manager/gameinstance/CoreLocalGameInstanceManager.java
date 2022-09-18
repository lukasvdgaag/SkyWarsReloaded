package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.util.concurrent.CompletableFuture;

public abstract class CoreLocalGameInstanceManager extends CoreGameInstanceManager implements LocalGameInstanceManager {

    public CoreLocalGameInstanceManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public GameInstance getGameInstanceBySWWorld(SWWorld swWorld) {
        if (swWorld == null) return null;
        for (GameInstance gameInstance : getGameInstancesList()) {
            if (gameInstance instanceof LocalGameInstance && ((LocalGameInstance) gameInstance).getWorld().equals(swWorld)) return gameInstance;
        }
        return null;
    }

    @Override
    public CompletableFuture<Boolean> saveInstanceToTemplate(GameInstance instance) {
        return plugin.getWorldLoader().save(instance);
    }

    @Override
    public CompletableFuture<Void> deleteGameInstance(GameInstance world) {
        plugin.getWorldLoader().deleteWorldInstance(world);
        return super.deleteGameInstance(world);
    }
}
