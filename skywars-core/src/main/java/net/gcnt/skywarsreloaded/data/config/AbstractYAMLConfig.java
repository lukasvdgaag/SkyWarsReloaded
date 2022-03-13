package net.gcnt.skywarsreloaded.data.config;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public abstract class AbstractYAMLConfig implements YAMLConfig {

    protected final AbstractSkyWarsReloaded plugin;

    private final String id;
    private final File file;
    private final String fileName;
    private final File directory;
    private final String directoryName;
    private String defaultFilePath;

    public AbstractYAMLConfig(AbstractSkyWarsReloaded pluginIn, String id, @Nullable String directoryName, String fileName) {
        this.id = id;
        this.plugin = pluginIn;
        this.fileName = fileName;
        this.directoryName = directoryName;

        if (directoryName != null) {
            // We are using a file that is not in the main plugin directory
            directory = new File(pluginIn.getDataFolder() + File.separator + directoryName);
        } else {
            // Else, use default plugin dir
            directory = pluginIn.getDataFolder();
        }

        file = new File(directory, fileName);

        if (directory.exists()) directory.mkdirs();
        if (!file.exists() && !this.copyDefaultFile()) {
            return;
        }
    }

    public AbstractYAMLConfig(AbstractSkyWarsReloaded pluginIn, String id, @Nullable String directoryName, String fileName, String defaultFilePath) {
        this.id = id;
        this.plugin = pluginIn;
        this.fileName = fileName;
        this.directoryName = directoryName;
        this.defaultFilePath = defaultFilePath;

        if (directoryName != null) {
            // We are using a file that is not in the main plugin directory
            directory = new File(pluginIn.getDataFolder() + File.separator + directoryName);
        } else {
            // Else, use default plugin dir
            directory = pluginIn.getDataFolder();
        }

        file = new File(directory, fileName);

        if (directory.exists()) directory.mkdirs();
        if (!file.exists() && !this.copyDefaultFile()) {
            return;
        }
    }

    public AbstractYAMLConfig(AbstractSkyWarsReloaded pluginIn, String id, File directory, String fileName) {
        this.id = id;
        this.plugin = pluginIn;
        this.fileName = fileName;
        this.directory = directory;
        this.directoryName = directory.getName();
        this.file = new File(directory, fileName);

        if (directory.exists()) directory.mkdirs();
        if (!file.exists() && !this.copyDefaultFile()) {
            return;
        }
    }

    public AbstractYAMLConfig(AbstractSkyWarsReloaded pluginIn, String id, File directory, String fileName, String defaultFilePath) {
        this.id = id;
        this.plugin = pluginIn;
        this.fileName = fileName;
        this.directory = directory;
        this.directoryName = directory.getName();
        this.file = new File(directory, fileName);
        this.defaultFilePath = defaultFilePath;

        if (directory.exists()) directory.mkdirs();
        if (!file.exists() && !this.copyDefaultFile()) {
            return;
        }
    }

    // Utils

    protected BufferedReader getDefaultFileReader() {
        this.defaultFilePath = this.defaultFilePath == null ? "/" + this.fileName : this.defaultFilePath;
        InputStream internalFileStream = getClass().getResourceAsStream(this.defaultFilePath);

        if (internalFileStream == null) {
            plugin.getLogger().error("Failed to load the default file of " + fileName);
            return null;
        }
        InputStreamReader isr = new InputStreamReader(internalFileStream);
        return new BufferedReader(isr);
    }

    public boolean copyDefaultFile() {
        BufferedReader defaultFileReader = this.getDefaultFileReader();

        File dataFolder = plugin.getDataFolder();

        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (SecurityException | IOException ex) {
            ex.printStackTrace();
            return false;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            String line;
            while ((line = defaultFileReader.readLine()) != null) {
                writer.write(line + "\n");
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // Getters

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDirectoryName() {
        return directoryName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public File getDirectory() {
        return directory;
    }

    @Override
    public File getFile() {
        return file;
    }

    protected String getDefaultFilePath() {
        return this.defaultFilePath;
    }

}
