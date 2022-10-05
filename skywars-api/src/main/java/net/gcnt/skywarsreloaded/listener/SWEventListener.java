package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.event.SWEvent;

public interface SWEventListener<T extends SWEvent> {

    void onEvent(T event);
    SWListenerPriority getPriority();

    Class<T> getEventClass();

}
