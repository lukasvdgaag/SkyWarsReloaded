package net.gcnt.skywarsreloaded.hook;

public interface SWHook {

    /**
     * Hook into the plugin this hook is for
     */
    void enable();

    /**
     * Every hook must be able to clean up after itself.
     * This includes removing event listeners, schedulers, etc.
     */
    void disable();

    /**
     * Get if this specific hook is enabled and hooked into the target plugin.
     * A hook can be considered inactive if it meets one of the following conditions:
     *  - The hook is manually instructed to unhook itself
     *  - The hook was unable to find its target API on the server
     *
     * @return True if the hook is active and working, false otherwise
     */
    boolean isEnabled();

}
