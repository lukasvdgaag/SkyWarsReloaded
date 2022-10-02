package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.hook.SWHook;

import java.util.HashMap;

public class CoreSWHookManager implements SWHookManager {

    private final HashMap<Class<? extends SWHook>, SWHook> hooks;

    public CoreSWHookManager(AbstractSkyWarsReloaded plugin) {
        this.hooks = new HashMap<>();
    }

    public <E extends SWHook> E getHook(Class<E> hookClass) {
        // noinspection unchecked
        return (E) hooks.get(hookClass);
    }

    @Override
    public void registerHook(SWHook hook) {
        this.hooks.put(hook.getClass(), hook);
    }

    @Override
    public void unregisterHook(SWHook hook) {
        this.hooks.remove(hook.getClass());
    }

    @Override
    public void enableAllHooks() {
        hooks.forEach((clazz, hook) -> hook.enable());
    }

    @Override
    public void disableAllHooks() {
        hooks.forEach((clazz, hook) -> hook.disable());
    }

    @Override
    public void unregisterAllHooks() {
        this.hooks.clear();
    }
}
