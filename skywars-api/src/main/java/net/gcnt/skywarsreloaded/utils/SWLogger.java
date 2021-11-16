package net.gcnt.skywarsreloaded.utils;

/**
 * The main logging class for the skywars plugin
 */
public interface SWLogger {

    /**
     * Logs a debug message
     *
     * @param message The message to log
     */
    void debug(String message);

    /**
     * Logs an info message
     *
     * @param message The message to log
     */
    void info(String message);

    /**
     * Logs a warning message
     *
     * @param message The message to log
     */
    void warn(String message);

    /**
     * Logs an error message
     *
     * @param message The message to log
     */
    void error(String message);

    /**
     * Report abnormal exception
     *
     * @param exception The exception to report
     */
    void reportException(Exception exception);

    /**
     * Get if debug mode is enabled
     * @return true if debug mode is enabled
     */
    boolean isDebugModeActive();

    /**
     * Set if debug mode is enabled
     * @param debugModeActive true if debug mode is enabled
     */
    void setDebugModeActive(boolean debugModeActive);

    /**
     * Get if the auto exception reporter is active
     *
     * @return true if the auto exception reporter is active
     */
    boolean isAutoExceptionReporterActive();

    /**
     * Set if the auto exception reporter is active
     *
     * @param autoExceptionReporterActive true if the auto exception reporter is active
     */
    void setAutoExceptionReporterActive(boolean autoExceptionReporterActive);

}
