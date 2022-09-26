package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.listener.SWAdvancedEventListener;

public interface SWEventManager {

    void unregisterListener(SWAdvancedEventListener<?> listener);

    void registerListener(SWAdvancedEventListener<?> listener);

    <T extends SWEvent> void callEvent(T event);

}
