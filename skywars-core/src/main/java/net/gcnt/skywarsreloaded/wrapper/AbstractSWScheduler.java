package net.gcnt.skywarsreloaded.wrapper;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

import java.util.function.Consumer;

public class AbstractSWScheduler implements SWScheduler {

    private final SkyWarsReloaded plugin;

    public AbstractSWScheduler(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runSync(Consumer<SWRunnable> runnable) {
        // todo: runnable.
    }

    @Override
    public void runAsync(Consumer<SWRunnable> runnable) {
        // todo
    }

    @Override
    public void runSyncLater(Consumer<SWRunnable> runnable, int ticks) {
        // todo
    }

    @Override
    public void runAsyncLater(Consumer<SWRunnable> runnable, int ticks) {
        // todo
    }
}
