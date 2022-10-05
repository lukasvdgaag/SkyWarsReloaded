package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.listener.SWEventListener;

public interface SWEventManager {

    void unregisterListener(SWEventListener<?> listener);

    void registerListener(SWEventListener<?> listener);

    <T extends SWEvent> void callEvent(T event);

}
