package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.AbstractSWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLManager;

public abstract class AbstractSkyWarsReloaded implements SkyWarsReloaded {

    private AbstractYAMLManager yamlManager;
    private Storage storage;

    public void onEnable() {
        // Configs
        this.yamlManager = this.createYAMLManager();
        this.yamlManager.loadAll();

        // todo storage based on config
    }

    // Internal Utils

    public abstract AbstractYAMLManager createYAMLManager();

    public abstract AbstractSWPlayerDataManager createPlayerDataManager();

    // Getters

    @Override
    public YAMLManager getYAMLManager() {
        return yamlManager;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

}
