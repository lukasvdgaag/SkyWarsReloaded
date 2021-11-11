package net.gcnt.skywarsreloaded.data.config;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface YAMLConfig {

    String getId();

    String getDirectoryName();

    String getFileName();

    File getDirectory();

    File getFile();

    String getString(String property);

    String getString(String property, String defaultValue);

    int getInt(String property);

    int getInt(String property, int defaultValue);

    List<String> getStringList(String property);

    double getDouble(String property);

    double getDouble(String property, double defaultValue);

    boolean getBoolean(String property);

    boolean getBoolean(String property, boolean defaultValue);

    Object get(String property);

    Object get(String property, Object defaultValue);

    boolean isset(String property);

    boolean contains(String property);

    Set<String> getKeys(String property);

    /**
     * Set a value to a key in the yaml config
     *
     * @param property The key to save the data to
     * @param value    The value to save
     */
    void set(String property, Object value);

    /**
     * Do not use this unless you know what you are doing! Use {@link YAMLManager} to perform saves instead.
     * This method is not actually deprecated but instead used as a visual alert.
     */
    @Deprecated
    void save();

}