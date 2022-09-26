package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.event.SWEvent;
import net.gcnt.skywarsreloaded.listener.SWAdvancedEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CoreSWEventManager implements SWEventManager {

    private final SkyWarsReloaded plugin;
    private final ArrayList<SWAdvancedEventListener<?>> listeners;

    public CoreSWEventManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void unregisterListener(SWAdvancedEventListener<?> listener) {
        synchronized (listeners) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public void registerListener(SWAdvancedEventListener<?> listener) {
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
        List<SWAdvancedEventListener<T>> relevantEventListeners;
        synchronized (listeners) {
            //noinspection unchecked
            relevantEventListeners = this.listeners.stream()
                    .filter(listener -> listener.getEventClass().isAssignableFrom(eventClass))
                    .sorted(Comparator.comparingInt(listener -> listener.getPriority().ordinal()))
                    .map(listener -> (SWAdvancedEventListener<T>) listener)
                    .collect(Collectors.toList());
        }
        relevantEventListeners.forEach(listener -> listener.onEvent(event));
    }
}
