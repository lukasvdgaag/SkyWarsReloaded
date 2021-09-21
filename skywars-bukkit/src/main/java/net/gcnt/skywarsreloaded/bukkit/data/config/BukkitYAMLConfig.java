package net.gcnt.skywarsreloaded.bukkit.data.config;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;

public class BukkitYAMLConfig extends AbstractYAMLConfig {

    private final Object reloadLock = new Object();
    private FileConfiguration fileConfiguration;

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String idIn, @Nullable String directory, String fileName) {
        super(skyWars, idIn, directory, fileName);

        this.onSetup(this.getFile());
    }

    protected boolean onSetup(File configFile) {
        synchronized (reloadLock) {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
            return true;
        }
    }

    protected boolean onDisable(boolean save) {
        if (!save) return true;
        try {
            synchronized (reloadLock) {
                this.fileConfiguration.save(this.getFile());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    @Override
    public String getString(String property) {
        return fileConfiguration.getString(property, "");
    }

    @Override
    public int getInt(String property) {
        return fileConfiguration.getInt(property, 0);
    }

    @Override
    public double getDouble(String property) {
        return fileConfiguration.getDouble(property, 0);
    }

    @Override
    public List<String> getStringList(String property) {
        return fileConfiguration.getStringList(property);
    }

    @Override
    public boolean getBoolean(String property) {
        return fileConfiguration.getBoolean(property, false);
    }
}
