package net.gcnt.skywarsreloaded.bukkit.data.config;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;

public class BukkitYAMLManager extends AbstractYAMLManager {

    public BukkitYAMLManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    public BukkitYAMLConfig createConfigInstance(String id, String directory, String fileName) {
        return new BukkitYAMLConfig(this.getSkyWars(), id, directory, fileName);
    }
}
