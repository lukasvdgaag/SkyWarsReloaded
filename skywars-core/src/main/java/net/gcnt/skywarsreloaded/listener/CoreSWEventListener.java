package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.event.SWEvent;

import java.util.function.Consumer;

public class CoreSWEventListener<T extends SWEvent> implements SWEventListener<T> {

    private final Class<T> typeClass;
    private final SWListenerPriority priority;
    private final Consumer<T> handler;

    public CoreSWEventListener(Class<T> typeClass, Consumer<T> handler) {
        this(typeClass, SWListenerPriority.NORMAL, handler);
    }

    public CoreSWEventListener(Class<T> typeClass, SWListenerPriority priority, Consumer<T> handler) {
        this.typeClass = typeClass;
        this.priority = priority;
        this.handler = handler;
    }

    @Override
    public SWListenerPriority getPriority() {
        return this.priority;
    }

    @Override
    public Class<T> getEventClass() {
        return this.typeClass;
    }

    @Override
    public void onEvent(T event) {
        this.handler.accept(event);
    }
}
