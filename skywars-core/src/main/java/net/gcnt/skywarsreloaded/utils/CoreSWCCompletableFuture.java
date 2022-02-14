package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CoreSWCCompletableFuture<T> implements SWCompletableFuture<T> {

    private final SkyWarsReloaded plugin;
    private final CompletableFuture<T> parent;

    public CoreSWCCompletableFuture(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
        this.parent = new CompletableFuture<>();
    }

    @Override
    public SWCompletableFuture<T> thenRunSync(Runnable task) {
        this.thenRun(() -> plugin.getScheduler().runSync(task));
        return this;
    }

    @Override
    public SWCompletableFuture<T> thenAcceptSync(Consumer<? super T> task) {
        this.thenAccept(returnVal -> plugin.getScheduler().runSync(() -> task.accept(returnVal)));
        return this;
    }

    @Override
    public SWCompletableFuture<T> thenAccept(Consumer<? super T> task) {
        parent.thenAccept(task);
        return this;
    }

    @Override
    public SWCompletableFuture<T> thenRun(Runnable task) {
        this.parent.thenRun(task);
        return this;
    }

    @Override
    public void complete(T value) {
        this.parent.complete(value);
    }

    // STATIC

    public static <T> CoreSWCCompletableFuture<T> completedFuture(SkyWarsReloaded pluginIn, T value) {
        CoreSWCCompletableFuture<T> swFuture = new CoreSWCCompletableFuture<>(pluginIn);
        swFuture.complete(value);
        return swFuture;
    }
}
