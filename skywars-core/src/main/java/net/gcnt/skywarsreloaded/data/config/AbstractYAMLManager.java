package net.gcnt.skywarsreloaded.data.config;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;

import java.util.HashMap;

public abstract class AbstractYAMLManager implements YAMLManager {

    private final AbstractSkyWarsReloaded skyWars;

    private final HashMap<String, AbstractYAMLConfig> files;

    public AbstractYAMLManager(AbstractSkyWarsReloaded skyWarsIn) {
        this.skyWars = skyWarsIn;
        this.files = new HashMap<>();
    }

    // Utils

    public void loadAll() {
        this.loadConfig("config", null, "config.yml");
    }

    public YAMLConfig loadConfig(String id, String directory, String fileName) {
        AbstractYAMLConfig config = this.createConfigInstance(id, directory, fileName);
        this.files.put(id, config);
        return config;
    }

    public abstract AbstractYAMLConfig createConfigInstance(String id, String directory, String fileName);

    // Getters

    public YAMLConfig getConfig(String file) {
        return this.files.getOrDefault(file, null);
    }

    public AbstractSkyWarsReloaded getSkyWars() {
        return this.skyWars;
    }

}
