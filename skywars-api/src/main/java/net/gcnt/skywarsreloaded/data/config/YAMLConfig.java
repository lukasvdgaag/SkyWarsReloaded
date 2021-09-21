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

    int getInt(String property);

    List<String> getStringList(String property);

    double getDouble(String property);

    boolean getBoolean(String property);


}
