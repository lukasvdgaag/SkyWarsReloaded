package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameStateHandler;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;

public abstract class CoreGameStateHandler implements GameStateHandler {

    protected final SkyWarsReloaded plugin;
    protected final LocalGameInstance gameInstance;

    public CoreGameStateHandler(SkyWarsReloaded plugin, LocalGameInstance gameInstance) {
        this.plugin = plugin;
        this.gameInstance = gameInstance;
    }

    @Override
    public LocalGameInstance getGameInstance() {
        return gameInstance;
    }

    @Override
    public SkyWarsReloaded getPlugin() {
        return plugin;
    }

    @Override
    public void tick() {

    }

    @Override
    public void tickSecond() {

    }
}
