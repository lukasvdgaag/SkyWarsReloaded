package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameStateHandler;

public abstract class CoreGameStateHandler implements GameStateHandler {

    protected final SkyWarsReloaded plugin;
    protected final GameInstance gameWorld;

    public CoreGameStateHandler(SkyWarsReloaded plugin, GameInstance gameWorld) {
        this.plugin = plugin;
        this.gameWorld = gameWorld;
    }

    @Override
    public GameInstance getGameWorld() {
        return gameWorld;
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
