package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.data.config.YAMLConfig;

import java.io.File;

public interface YAMLManager {

    /**
     * Load config or create it if nonexistent
     *
     * @param id        The internal ID of the config
     * @param directory The sub-directory in which to place the config file when created
     * @param fileName  The name of the file placed in the previously mentioned directory
     * @return A YAMLConfig instance based on the arguments provided
     */
    YAMLConfig loadConfig(String id, File directory, String fileName);

    /**
     * Load config or create it if nonexistent
     *
     * @param id        The internal ID of the config
     * @param directory The sub-directory in which to place the config file when created
     * @param fileName  The name of the file placed in the previously mentioned directory
     * @return A YAMLConfig instance based on the arguments provided
     */
    YAMLConfig loadConfig(String id, String directory, String fileName);

    /**
     * Load config or create it if nonexistent
     *
     * @param id         The internal ID of the config
     * @param directory  The sub-directory in which to place the config file when created
     * @param fileName   The name of the file placed in the previously mentioned directory
     * @param defaultDir The internal directory from which to fetch the default file
     * @return A YAMLConfig instance based on the arguments provided
     */
    YAMLConfig loadConfig(String id, File directory, String fileName, String defaultDir);

    /**
     * Load config or create it if nonexistent
     *
     * @param id         The internal ID of the config
     * @param directory  The sub-directory in which to place the config file when created
     * @param fileName   The name of the file placed in the previously mentioned directory
     * @param defaultDir The internal directory from which to fetch the default file
     * @return A YAMLConfig instance based on the arguments provided
     */
    YAMLConfig loadConfig(String id, String directory, String fileName, String defaultDir);

    /**
     * Get a previously initialized config file by config ID
     *
     * @param configId The internal config ID of the config
     * @return The YAMLConfig instance with the ID provided or null if no YAMLConfig exists with that ID
     */
    YAMLConfig getConfig(String configId);

}
