package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameStateHandler;
import net.gcnt.skywarsreloaded.game.GameWorld;

public abstract class CoreGameStateHandler implements GameStateHandler {

    protected final SkyWarsReloaded plugin;
    protected final GameWorld gameWorld;

    public CoreGameStateHandler(SkyWarsReloaded plugin, GameWorld gameWorld) {
        this.plugin = plugin;
        this.gameWorld = gameWorld;
    }

    @Override
    public GameWorld getGameWorld() {
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
