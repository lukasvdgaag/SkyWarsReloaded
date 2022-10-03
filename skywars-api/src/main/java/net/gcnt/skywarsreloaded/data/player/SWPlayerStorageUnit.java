package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface SWPlayerStorageUnit {

    /**
     * Loads all data from storage (MySQL/MongoDB/YAML) into cache.
     */
    void loadData(SWPlayer player);

    /**
     * Saves all cache data to storage (MySQL/MongoDB/YAML).
     */
    void saveData(SWPlayer player);

}
