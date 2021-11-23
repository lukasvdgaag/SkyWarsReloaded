package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

public abstract class AbstractWorldLoader implements GameWorldLoader {

    public final SkyWarsReloaded plugin;

    public AbstractWorldLoader(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

}
