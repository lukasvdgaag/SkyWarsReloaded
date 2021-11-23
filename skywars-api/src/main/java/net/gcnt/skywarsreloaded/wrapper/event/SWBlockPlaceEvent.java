package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWBlockPlaceEvent extends SWPlayerEvent, SWCancellable {

    SWCoord getCoord();

    String getBlockTypeName();

}
