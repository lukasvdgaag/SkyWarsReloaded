package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface SWPlayerEvent extends SWEvent {

    SWPlayer getPlayer();

}
