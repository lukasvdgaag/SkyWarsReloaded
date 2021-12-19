package net.gcnt.skywarsreloaded.game.loader;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public abstract class AbstractWorldLoader implements GameWorldLoader {

    public final SkyWarsReloaded plugin;

    public AbstractWorldLoader(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createBasePlatform(GameWorld gameWorld) {
        SWWorld world = gameWorld.getWorld();
        if (!world.isLoaded()) return;
        world.setBlockAt(new CoreSWCoord(gameWorld.getWorld(), InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y,
                InternalProperties.MAP_CREATE_PLATFORM_Z), "stone");
    }
}
