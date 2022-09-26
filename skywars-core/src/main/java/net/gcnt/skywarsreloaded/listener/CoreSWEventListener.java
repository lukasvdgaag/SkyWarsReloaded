package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.event.SWEvent;

public abstract class CoreSWEventListener<T extends SWEvent> extends CoreSWAdvancedEventListener<T> {

    public CoreSWEventListener() {
        super(SWListenerPriority.NORMAL);
    }

}
