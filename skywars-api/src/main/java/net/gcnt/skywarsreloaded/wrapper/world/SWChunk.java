package net.gcnt.skywarsreloaded.wrapper.world;

public interface SWChunk {

    boolean isLoaded();

    void load();

    void unload();

    SWWorld getWorld();

}
