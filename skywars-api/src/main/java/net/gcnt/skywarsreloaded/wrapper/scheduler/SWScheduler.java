package net.gcnt.skywarsreloaded.wrapper.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface SWScheduler {

    /**
     * Schedule runnable to run synchronously now and return a CompletableFuture.
     *
     * @param supplier Supplier that returns data from the sync method. This data can be null
     * @return CompletableFuture
     */
    <T> CompletableFuture<T> callSyncMethod(Supplier<T> supplier);

    /**
     * Schedule runnable to run synchronously now.
     *
     * @param runnable Skywars runnable
     */
    void runSync(Runnable runnable);

    /**
     * Schedule runnable to run asynchronously now.
     *
     * @param runnable Skywars runnable
     */
    void runAsync(Runnable runnable);

    /**
     * Schedule runnable to run synchronously later.
     *
     * @param runnable Skywars runnable
     * @param ticks    Delay in ticks
     */
    void runSyncLater(Runnable runnable, int ticks);

    /**
     * Schedule runnable to run asynchronously later.
     *
     * @param runnable Skywars runnable
     * @param ticks    Delay in ticks
     */
    void runAsyncLater(Runnable runnable, int ticks);

    /**
     * Schedule runnable to run synchronously on a timer
     *
     * @param runnable Skywars runnable
     * @param ticks    Delay in ticks
     * @param period   Period in ticks
     */
    SWRunnable runSyncTimer(SWRunnable runnable, int ticks, int period);

    /**
     * Schedule runnable to run asynchronously on a timer
     *
     * @param runnable Skywars runnable
     * @param ticks    Delay in ticks
     * @param period   Period in ticks
     */
    SWRunnable runAsyncTimer(SWRunnable runnable, int ticks, int period);
}
