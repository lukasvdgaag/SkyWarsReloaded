package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

public interface SWEvent {

    SkyWarsReloaded getPlugin();

    Class<? extends SWEvent> getType();
    
}
