package net.gcnt.skywarsreloaded.wrapper;

public interface SWRunnable extends Runnable {

    void cancel();

    boolean isCancelled();

}