package net.gcnt.skywarsreloaded.data.config;

public interface YAMLManager {

    YAMLConfig loadConfig(String id, String directory, String fileName);

    YAMLConfig getConfig(String file);

}
