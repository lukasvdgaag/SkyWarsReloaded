package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameWorld;

public abstract class AbstractWorldLoader implements GameWorldLoader {

    public final SkyWarsReloaded plugin;

    public AbstractWorldLoader(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public abstract void generateWorld(GameWorld gameWorld);

    @Override
    public abstract void createEmptyWorld(GameWorld gameWorld);

    @Override
    public abstract void loadSchematic(GameWorld gameWorld);

    @Override
    public abstract void deleteWorld(GameWorld gameWorld);
}
