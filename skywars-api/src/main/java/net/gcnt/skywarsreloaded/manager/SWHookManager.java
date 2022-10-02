package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.hook.SWHook;

public interface SWHookManager {

    /**
     * Registers a hook into SkyWars. If this hook is registered before the {@link SkyWarsReloaded#onEnable()}
     * method is called, it will be automatically enabled. Otherwise, you will need to register this hook then
     * manually trigger its enable method.
     *
     * @param hook The {@link SWHook} to register.
     */
    void registerHook(SWHook hook);

    <E extends SWHook> E getHook(Class<E> hookClass);

    /**
     * Unregisters a hook from SkyWars. Make sure you disable the hook as well, this method won't do that
     * for you.
     *
     * @param hook
     */
    void unregisterHook(SWHook hook);

    /**
     * Enables all the hooks in the registry. Do not call this to enable custom hooks, you will break the plugin.
     * See {@link #registerHook(SWHook)}
     */
    void enableAllHooks();

    /**
     * Iterates over every {@link SWHook} and calls the {@link SWHook#disable()} method.
     */
    void disableAllHooks();

    /**
     * Unregisters all the hooks from SkyWars. You probably want to call {@link #disableAllHooks()} too.
     */
    void unregisterAllHooks();
}
