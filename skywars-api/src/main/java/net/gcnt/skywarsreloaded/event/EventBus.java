package net.gcnt.skywarsreloaded.event;

import java.util.Set;
import java.util.function.Consumer;

public interface EventBus {

    <T extends SWEvent> EventSubscription<T> subscribe(Class<T> eventClass, Consumer<? super T> handler);

    <T extends SWEvent> EventSubscription<T> subscribe(Object plugin, Class<T> eventClass, Consumer<? super T> handler);

    <T extends SWEvent> Set<EventSubscription<T>> getSubscriptions(Class<T> eventClass);

}
