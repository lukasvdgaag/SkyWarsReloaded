package net.gcnt.skywarsreloaded.wrapper.scheduler;

public abstract class CoreSWRunnable implements SWRunnable {

    private boolean cancelled;

    public CoreSWRunnable() {
        this.cancelled = false;
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
