package net.gcnt.skywarsreloaded.bukkit;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.data.BukkitYAMLConfig;
import net.gcnt.skywarsreloaded.data.Storage;
import net.gcnt.skywarsreloaded.data.YAMLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSkyWarsReloaded extends JavaPlugin implements SkyWarsReloaded {

    private YAMLManager yamlManager;
    private Storage storage;

    @Override
    public void onEnable() {
        super.onEnable();

        this.yamlManager = new YAMLManager(this);
        getYAMLManager().loadFile("config.yml", new BukkitYAMLConfig(this, null, "config.yml"));
        // todo storage based on config
    }

    @Override
    public YAMLManager getYAMLManager() {
        return yamlManager;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }
}
