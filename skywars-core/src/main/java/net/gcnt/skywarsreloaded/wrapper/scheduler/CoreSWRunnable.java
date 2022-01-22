package net.gcnt.skywarsreloaded.wrapper.scheduler;

public abstract class CoreSWRunnable implements SWRunnable {

    private boolean cancelled;
    private int taskId;

    public CoreSWRunnable() {
        this.cancelled = false;
    }

    public CoreSWRunnable(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
