package net.gcnt.skywarsreloaded.data.config;

import java.io.File;

public interface YAMLManager {

    YAMLConfig loadConfig(String id, File directory, String fileName);

    YAMLConfig getConfig(String file);

}
