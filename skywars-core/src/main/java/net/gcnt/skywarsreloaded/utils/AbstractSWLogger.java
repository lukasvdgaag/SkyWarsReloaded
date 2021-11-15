package net.gcnt.skywarsreloaded.utils;

public abstract class AbstractSWLogger implements SWLogger {

    private boolean debugModeActive;

    public AbstractSWLogger(boolean debugModeActive) {
        this.debugModeActive = debugModeActive;
    }

    @Override
    public void debug(String message) {
        if (!debugModeActive) return;
        info(message);
    }

    @Override
    public boolean isDebugModeActive() {
        return false;
    }

    @Override
    public void setDebugModeActive(boolean debugModeActive) {
        this.debugModeActive = debugModeActive;
    }
}
