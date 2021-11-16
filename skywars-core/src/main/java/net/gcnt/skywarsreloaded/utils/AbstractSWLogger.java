package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.gcntTelemetry.CoreSWExceptionReporter;

public abstract class AbstractSWLogger implements SWLogger {

    private final SkyWarsReloaded plugin;

    private boolean debugModeActive;
    private boolean autoExceptionReportActive;

    private CoreSWExceptionReporter exceptionReporter;

    public AbstractSWLogger(SkyWarsReloaded pluginIn, boolean debugModeActive) {
        this.plugin = pluginIn;
        this.debugModeActive = debugModeActive;
        this.autoExceptionReportActive = false;

        this.exceptionReporter = new CoreSWExceptionReporter(this.plugin);
    }

    @Override
    public void debug(String message) {
        if (!debugModeActive) return;
        info(message);
    }

    @Override
    public void reportException(Exception exception) {
        if (!autoExceptionReportActive) return;
        // todo: Make this async - this.exceptionReporter.reportException(exception);
    }

    @Override
    public boolean isDebugModeActive() {
        return false;
    }

    @Override
    public void setDebugModeActive(boolean debugModeActive) {
        this.debugModeActive = debugModeActive;
    }

    @Override
    public boolean isAutoExceptionReporterActive() {
        return this.autoExceptionReportActive;
    }

    @Override
    public void setAutoExceptionReporterActive(boolean autoReportActive) {
        this.autoExceptionReportActive = autoReportActive;
    }
}
