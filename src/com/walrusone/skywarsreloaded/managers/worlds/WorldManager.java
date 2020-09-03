package com.walrusone.skywarsreloaded.managers.worlds;

import org.bukkit.World;

import java.io.File;

public interface WorldManager {

    World createEmptyWorld(String name, World.Environment environment);

    boolean loadWorld(String worldName, World.Environment environment);

    void unloadWorld(String world, boolean save);

    void copyWorld(File source, File target);

    /**
     *
     * @param name Name of the world to remove
     * @param removeFile Only applies to {@link SWMWorldManager#deleteWorld(String, boolean)}, whether to delete the world folder.
     */
    void deleteWorld(String name, boolean removeFile);

    void deleteWorld(File file);

}
