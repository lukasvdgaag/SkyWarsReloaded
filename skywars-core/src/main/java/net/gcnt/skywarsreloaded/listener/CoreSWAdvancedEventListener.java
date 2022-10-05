package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.event.SWEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CoreSWAdvancedEventListener<T extends SWEvent> implements SWAdvancedEventListener<T> {

    private final Class<T> typeClass;
    private SWListenerPriority priority;

    public CoreSWAdvancedEventListener(SWListenerPriority priority) {
        this.priority = priority;

        Class<T> tmpTypeClass;
        try {
            Type genericSuper = this.getClass().getGenericSuperclass();
            Type typeArg = ((ParameterizedType) genericSuper).getActualTypeArguments()[0];
            String className = typeArg.toString();
            // noinspection unchecked
            tmpTypeClass = (Class<T>) Class.forName(className);
        } catch (Exception ignored) {
            tmpTypeClass = null;
        }
        this.typeClass = tmpTypeClass;
    }

    @Override
    public SWListenerPriority getPriority() {
        return this.priority;
    }

    @Override
    public Class<T> getEventClass() {
        return typeClass;
    }
}
