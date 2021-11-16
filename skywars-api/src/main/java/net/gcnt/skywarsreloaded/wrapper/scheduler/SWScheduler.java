package net.gcnt.skywarsreloaded.wrapper.scheduler;

public interface SWScheduler {

    /**
     * Schedule runnable to run synchronously now.
     *
     * @param runnable Skywars runnable
     */
    void runSync(SWRunnable runnable);

    /**
     * Schedule runnable to run asynchronously now.
     *
     * @param runnable Skywars runnable
     */
    void runAsync(SWRunnable runnable);

    /**
     * Schedule runnable to run synchronously later.
     *
     * @param runnable Skywars runnable
     * @param ticks Delay in ticks
     */
    void runSyncLater(SWRunnable runnable, int ticks);

    /**
     * Schedule runnable to run asynchronously later.
     *
     * @param runnable Skywars runnable
     * @param ticks Delay in ticks
     */
    void runAsyncLater(SWRunnable runnable, int ticks);

    /**
     * Schedule runnable to run synchronously on a timer
     *
     * @param runnable Skywars runnable
     * @param ticks Delay in ticks
     * @param period Period in ticks
     */
    void runSyncTimer(SWRunnable runnable, int ticks, int period);

    /**
     * Schedule runnable to run asynchronously on a timer
     *
     * @param runnable Skywars runnable
     * @param ticks Delay in ticks
     * @param period Period in ticks
     */
    void runAsyncTimer(SWRunnable runnable, int ticks, int period);
}
