package net.gcnt.skywarsreloaded.event;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.kyori.event.EventSubscriber;
import net.kyori.event.SimpleEventBus;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractEventBus<P> implements EventBus, AutoCloseable {

    private final SkyWarsReloaded plugin;

    private final Bus bus = new Bus();

    protected AbstractEventBus(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    public SkyWarsReloaded getPlugin() {
        return plugin;
    }

    protected abstract P checkPlugin(Object plugin) throws IllegalArgumentException;

    public void post(SWEvent event) {
        this.bus.post(event);
    }

    public boolean shouldPost(Class<? extends SWEvent> eventClass) {
        return this.bus.hasSubscribers(eventClass);
    }

    public void subscribe(SWEventListener listener) {
        listener.bind(this);
    }

    @Override
    public <T extends SWEvent> EventSubscription<T> subscribe(Class<T> eventClass, Consumer<? super T> handler) {
        Objects.requireNonNull(eventClass, "eventClass");
        Objects.requireNonNull(handler, "handler");

        return registerSubscription(eventClass, handler, null);
    }

    @Override
    public <T extends SWEvent> EventSubscription<T> subscribe(Object plugin, Class<T> eventClass, Consumer<? super T> handler) {
        Objects.requireNonNull(eventClass, "eventClass");
        Objects.requireNonNull(handler, "handler");
        Objects.requireNonNull(handler, "handler");

        return registerSubscription(eventClass, handler, checkPlugin(plugin));
    }

    private <T extends SWEvent> EventSubscription<T> registerSubscription(Class<T> eventClass, Consumer<? super T> handler, Object plugin) {
        if (!eventClass.isInterface()) {
            throw new IllegalArgumentException("Class " + eventClass.getName() + " must be an interface");
        }
        if (SWEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException("Class " + eventClass.getName() + " must implement SWEvent");
        }

        CoreSWEventSubscription<T> eventHandler = new CoreSWEventSubscription<>(this, eventClass, handler, plugin);
        this.bus.register(eventClass, eventHandler);

        return eventHandler;
    }

    @Override
    public <T extends SWEvent> Set<EventSubscription<T>> getSubscriptions(Class<T> eventClass) {
        return this.bus.getHandlers(eventClass);
    }

    public void unregisterHandler(CoreSWEventSubscription<?> handler) {
        this.bus.unregister(handler);
    }

    protected void unregisterHandlers(P plugin) {
        this.bus.unregister(sub -> ((CoreSWEventSubscription<?>) sub).getPlugin() == plugin);
    }

    @Override
    public void close() throws Exception {
        this.bus.unregisterAll();
    }

    private static final class Bus extends SimpleEventBus<SWEvent> {
        Bus() {
            super(SWEvent.class);
        }

        @Override
        protected boolean shouldPost(@Nonnull SWEvent event, @Nonnull EventSubscriber<?> subscriber) {
            return true;
        }

        public <T extends SWEvent> Set<EventSubscription<T>> getHandlers(Class<T> eventClass) {
            //noinspection unchecked
            return super.subscribers().values().stream()
                    .filter(s -> s instanceof EventSubscription && ((EventSubscription<?>) s).getEventClass().isAssignableFrom(eventClass))
                    .map(s -> (EventSubscription<T>) s)
                    .collect(Collectors.toSet());
        }

    }

}
