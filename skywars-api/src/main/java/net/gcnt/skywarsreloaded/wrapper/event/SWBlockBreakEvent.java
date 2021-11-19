package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWBlockBreakEvent extends SWPlayerEvent, SWCancellable{

    SWCoord getCoord();

    String getBlockTypeName();
    
    String getWorldName();

}
