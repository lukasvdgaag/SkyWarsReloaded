package net.gcnt.skywarsreloaded.event;

import net.kyori.event.EventSubscriber;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CoreSWEventSubscription<T extends SWEvent> implements EventSubscription<T>, EventSubscriber<T> {

    private final AbstractEventBus<?> eventBus;
    private final Class<T> eventClass;
    private final Consumer<? super T> consumer;
    private final Object plugin;
    private final AtomicBoolean active;

    public CoreSWEventSubscription(AbstractEventBus<?> eventBus, Class<T> eventClass, Consumer<? super T> consumer, Object plugin) {
        this.eventBus = eventBus;
        this.eventClass = eventClass;
        this.consumer = consumer;
        this.plugin = plugin;
        this.active = new AtomicBoolean(true);
    }

    @Override
    public boolean isActive() {
        return this.active.get();
    }

    @Override
    public void close() {
        if (!this.active.getAndSet(false)) return;

        this.eventBus.unregisterHandler(this);
    }

    @Override
    public void invoke(@Nonnull T event) {
        try {
            this.consumer.accept(event);
        } catch (Throwable t) {
            this.eventBus.getPlugin().getLogger().warn("Unable to pass event " + event.getType().getSimpleName() + " to handler " + this.consumer.getClass().getName());
        }
    }

    @Override
    public Class<T> getEventClass() {
        return eventClass;
    }

    @Override
    public Consumer<? super T> getHandler() {
        return this.consumer;
    }

    public Object getPlugin() {
        return plugin;
    }
}
