package net.gcnt.skywarsreloaded.utils;

import java.util.function.Consumer;

public interface SWCompletableFuture<T> {

    SWCompletableFuture<T> thenRun(Runnable task);

    SWCompletableFuture<T> thenAccept(Consumer<? super T> task);

    SWCompletableFuture<T> thenRunSync(Runnable task);

    SWCompletableFuture<T> thenAcceptSync(Consumer<? super T> task);

    void complete(T value);

}
