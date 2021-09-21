package net.gcnt.skywarsreloaded.data;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

import java.util.HashMap;

public class YAMLManager {

    private final HashMap<String, YAMLConfig> files;
    private final SkyWarsReloaded plugin;

    public YAMLManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.files = new HashMap<>();
    }

    public void loadFile(String name, YAMLConfig file) {
        this.files.put(name, file);
    }

    public YAMLConfig getFile(String file) {
        return this.files.getOrDefault(file, null);
    }

}
