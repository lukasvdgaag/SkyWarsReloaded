package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.listener.SWEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CoreSWEventManager implements SWEventManager {

    private final SkyWarsReloaded plugin;
    private final ArrayList<SWEventListener<?>> listeners;

    public CoreSWEventManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void unregisterListener(SWEventListener<?> listener) {
        synchronized (listeners) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public void registerListener(SWEventListener<?> listener) {
        synchronized (listeners) {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener);
            }
        }
    }

    @Override
    public <T extends SWEvent> void callEvent(T event) {
        @SuppressWarnings("unchecked")
        Class<T> eventClass = (Class<T>) event.getClass();
        List<SWEventListener<T>> relevantEventListeners;
        synchronized (listeners) {
            //noinspection unchecked
            relevantEventListeners = this.listeners.stream()
                    .filter(listener -> listener.getEventClass().isAssignableFrom(eventClass))
                    .sorted(Comparator.comparingInt(listener -> listener.getPriority().ordinal()))
                    .map(listener -> (SWEventListener<T>) listener)
                    .collect(Collectors.toList());
        }
        relevantEventListeners.forEach(listener -> listener.onEvent(event));
    }
}
