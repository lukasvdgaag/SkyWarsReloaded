package net.gcnt.skywarsreloaded.wrapper.scheduler;

public interface SWRunnable extends Runnable {

    void cancel();

    boolean isCancelled();

}