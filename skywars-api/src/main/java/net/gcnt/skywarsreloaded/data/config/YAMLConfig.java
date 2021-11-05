package net.gcnt.skywarsreloaded.data.config;

import java.io.File;
import java.util.List;

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

}
