package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface SWChunkLoadEvent extends SWEvent {

    SWWorld getWorld();

    int getX();

    int getZ();

    boolean isGeneratingChunk();

}
