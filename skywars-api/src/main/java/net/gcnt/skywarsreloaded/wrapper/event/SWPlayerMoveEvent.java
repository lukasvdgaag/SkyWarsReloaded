package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.SWCoord;

public interface SWPlayerMoveEvent extends SWPlayerEvent, SWCancellable {

    SWCoord getFrom();

    SWCoord getTo();

}
