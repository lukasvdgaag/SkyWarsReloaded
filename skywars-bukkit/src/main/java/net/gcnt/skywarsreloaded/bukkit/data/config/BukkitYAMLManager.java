package net.gcnt.skywarsreloaded.bukkit.data.config;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLManager;

import java.io.File;

public class BukkitYAMLManager extends AbstractYAMLManager {

    public BukkitYAMLManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    public BukkitYAMLConfig createConfigInstance(String id, File directory, String fileName) {
        return createConfigInstance(id, directory, fileName, null);
    }

    public BukkitYAMLConfig createConfigInstance(String id, File directory, String fileName, String defaultDir) {
        return new BukkitYAMLConfig(this.getSkyWars(), id, directory, fileName, defaultDir);
    }

    public BukkitYAMLConfig createConfigInstance(String id, String directory, String fileName) {
        return createConfigInstance(id, directory, fileName, null);
    }

    public BukkitYAMLConfig createConfigInstance(String id, String directory, String fileName, String defaultDir) {
        return new BukkitYAMLConfig(this.getSkyWars(), id, directory, fileName, defaultDir);
    }
}
