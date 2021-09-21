package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.wrapper.AbstractSWPlayer;

public interface Storage { // this needs an interface?

    /**
     * Will set up the requirements for the storage to function properly.
     */
    void setup();


    /**
     * Loads all data of a player from storage (MySQL/MongoDB/YAML) into cache.
     *
     * @param player Player to load the data of.
     */
    void loadData(AbstractSWPlayer player);

    /**
     * Updates a specific property in the selected storage method.
     *
     * @param property Name of the property.
     * @param value    Value of the property.
     */
    void set(String property, Object value, AbstractSWPlayer player);


}
