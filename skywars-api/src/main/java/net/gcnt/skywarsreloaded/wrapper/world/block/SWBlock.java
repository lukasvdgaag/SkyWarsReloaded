package net.gcnt.skywarsreloaded.wrapper.world.block;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.world.SWChunk;

public interface SWBlock {

    // Getters

    SWChunk getChunk();

    SWCoord getCoord();

    String getMaterial();

}
