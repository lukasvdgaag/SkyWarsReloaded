package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface SWWorldInitEvent extends SWEvent {

    SWWorld getWorld();

}
