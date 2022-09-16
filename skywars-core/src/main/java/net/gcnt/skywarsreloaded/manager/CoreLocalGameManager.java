package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.LocalGameInstance;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public abstract class CoreLocalGameManager extends CoreGameManager implements LocalGameManager {

    public CoreLocalGameManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public GameInstance getGameInstanceBySWWorld(SWWorld swWorld) {
        if (swWorld == null) return null;
        for (GameInstance gameInstance : getGameInstancesListCopy()) {
            if (gameInstance instanceof LocalGameInstance && ((LocalGameInstance) gameInstance).getWorld().equals(swWorld)) return gameInstance;
        }
        return null;
    }

}
