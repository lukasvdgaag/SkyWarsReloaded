package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;

import java.io.File;
import java.util.HashMap;

public abstract class AbstractYAMLManager implements YAMLManager {

    private final AbstractSkyWarsReloaded skyWars;

    private final HashMap<String, AbstractYAMLConfig> files;

    public AbstractYAMLManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.skyWars = skyWarsIn;
        this.files = new HashMap<>();
    }

    // Utils

    public YAMLConfig loadConfig(String id, File directory, String fileName) {
        AbstractYAMLConfig config = this.createConfigInstance(id, directory, fileName, null);
        this.files.put(id, config);
        return config;
    }

    public YAMLConfig loadConfig(String id, File directory, String fileName, String defaultDir) {
        AbstractYAMLConfig config = this.createConfigInstance(id, directory, fileName, defaultDir);
        this.files.put(id, config);
        return config;
    }

    public YAMLConfig loadConfig(String id, String directory, String fileName, String defaultDir) {
        AbstractYAMLConfig config = this.createConfigInstance(id, directory, fileName, defaultDir);
        this.files.put(id, config);
        return config;
    }

    public YAMLConfig loadConfig(String id, String directory, String fileName) {
        AbstractYAMLConfig config = this.createConfigInstance(id, directory, fileName, null);
        this.files.put(id, config);
        return config;
    }

    public abstract AbstractYAMLConfig createConfigInstance(String id, File directory, String fileName, String defaultDir);

    public abstract AbstractYAMLConfig createConfigInstance(String id, File directory, String fileName);

    public abstract AbstractYAMLConfig createConfigInstance(String id, String directory, String fileName);

    public abstract AbstractYAMLConfig createConfigInstance(String id, String directory, String fileName, String defaultDir);

    // Getters

    public YAMLConfig getConfig(String file) {
        return this.files.getOrDefault(file, null);
    }

    public AbstractSkyWarsReloaded getSkyWars() {
        return this.skyWars;
    }

}
