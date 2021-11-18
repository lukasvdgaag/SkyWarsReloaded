package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWBlockPlaceEvent {

    SWCoord getCoord();

    String getBlockTypeName();
    
    String getWorldName();

}
