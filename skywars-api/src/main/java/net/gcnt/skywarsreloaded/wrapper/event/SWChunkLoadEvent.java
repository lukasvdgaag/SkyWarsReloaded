package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface SWChunkLoadEvent {

    SWWorld getWorld();

    int getX();

    int getZ();

    boolean isGeneratingChunk();

}
