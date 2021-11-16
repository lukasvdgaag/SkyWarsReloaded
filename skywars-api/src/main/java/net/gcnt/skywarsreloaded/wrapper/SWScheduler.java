package net.gcnt.skywarsreloaded.wrapper;

import java.util.function.Consumer;

public interface SWScheduler {

    /**
     * Schedule runnable to run synchronously now.
     *
     * @param runnable Skywars runnable
     */
    void runSync(Consumer<SWRunnable> runnable);

    /**
     * Schedule runnable to run asynchronously now.
     *
     * @param runnable Skywars runnable
     */
    void runAsync(Consumer<SWRunnable> runnable);

    /**
     * Schedule runnable to run synchronously later.
     *
     * @param runnable Skywars runnable
     * @param ticks Delay in ticks
     */
    void runSyncLater(Consumer<SWRunnable> runnable, int ticks);

    /**
     * Schedule runnable to run asynchronously later.
     *
     * @param runnable Skywars runnable
     * @param ticks Delay in ticks
     */
    void runAsyncLater(Consumer<SWRunnable> runnable, int ticks);
}
