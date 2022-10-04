package net.gcnt.skywarsreloaded.event;

import java.util.function.Consumer;

public interface EventSubscription<T extends SWEvent> extends AutoCloseable {

    Class<T> getEventClass();

    boolean isActive();

    Consumer<? super T> getHandler();

    @Override
    void close();
}
