package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public interface Storage {

    /**
     * Will set up the requirements for the storage to function properly.
     */
    void setup();


    /**
     * Loads all data from storage (MySQL/MongoDB/YAML) into cache.
     */
    void loadData(SWPlayer player);

    /**
     * Saves all cache data to storage (MySQL/MongoDB/YAML).
     */
    void saveData();

    /**
     * Sets a property for a player
     *
     * @param property
     * @param value
     * @param player
     */
    void setProperty(String property, Object value, SWPlayer player);

}
