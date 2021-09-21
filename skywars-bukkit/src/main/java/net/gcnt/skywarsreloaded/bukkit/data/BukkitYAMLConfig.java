package net.gcnt.skywarsreloaded.bukkit.data;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.YAMLConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;

public class BukkitYAMLConfig implements YAMLConfig {
    protected final BukkitSkyWarsReloaded plugin;
    private final Object reloadLock = new Object();
    private final File file;
    private final String fileName;
    private final String fileDirectory;
    private FileConfiguration fileConfiguration;

    public BukkitYAMLConfig(BukkitSkyWarsReloaded plugin, @Nullable String directory, String fileName) {
        this.plugin = plugin;
        this.fileDirectory = directory;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (directory != null)
            file = new File(plugin.getDataFolder() + File.separator + directory, fileName);
        else file = new File(plugin.getDataFolder(), fileName);

        if (!dataFolder.exists() || !file.exists()) {
            if (!this.copyDefaultFile()) {
                return;
            }
        }

        this.onSetup(file);
    }

    public boolean copyDefaultFile() {
        InputStream internalFileStream = getClass().getResourceAsStream("/" + fileName);
        if (internalFileStream == null) {
            return false;
        }
        InputStreamReader isr = new InputStreamReader(internalFileStream);
        BufferedReader bufferedReader = new BufferedReader(isr);

        File dataFolder = plugin.getDataFolder();

        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
            return false;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                writer.write(line + "\n");
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
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

    @Override
    public File getFile() {
        return file;
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

    @Override
    public String getDirectory() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
