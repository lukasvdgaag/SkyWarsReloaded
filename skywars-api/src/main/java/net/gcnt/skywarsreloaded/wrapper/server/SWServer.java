package net.gcnt.skywarsreloaded.wrapper.server;

import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface SWServer {

    /**
     * Get the wrapper to the default world. Minecraft is required to have this loaded at all times.
     *
     * @return The default world
     */
    SWWorld getDefaultWorld();

    /**
     * Get a world by it's name
     *
     * @return The world with the name provided
     */
    SWWorld getWorld(String name);

    /**
     * ...
     */

}
