package net.gcnt.skywarsreloaded.data;

public interface Storage { // this needs an interface?

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

}
