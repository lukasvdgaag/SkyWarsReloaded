package com.walrusone.skywarsreloaded.utilities;

import com.google.common.collect.ImmutableMap;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * This is not used at the moment, WIP
 */
public class WorldOptions {

    @Nullable
    private World world;
    private final Object worldLock = new Object();

    private HashMap<String, Object> gamerules;
    private final Object gamerulesLock = new Object();

    public WorldOptions(World worldIn, HashMap<String, Object> gamerulesIn) {
        this.world = worldIn;
        this.gamerules = gamerulesIn;
    }

    // GETTERS

    public World getWorld() {
        synchronized (worldLock) {
            return this.world;
        }
    }

    public ImmutableMap<String, Object> getGamerulesCopy() {
        synchronized (gamerulesLock) {
            return ImmutableMap.copyOf(this.gamerules);
        }
    }

    // SETTERS

    public void setGamerule(String key, Object value) {
        synchronized (gamerulesLock) {
            this.gamerules.put(key, value);
        }
    }

    public void removeGamerule(String key) {
        synchronized (gamerulesLock) {
            this.gamerules.remove(key);
        }
    }

}
