package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractChestManager implements ChestManager {

    public final SkyWarsReloaded plugin;
    public HashMap<String, SWChestType> chests;

    public AbstractChestManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.chests = new HashMap<>();
    }

    @Override
    public void loadAllChestTypes() {
        // Chests folder under skywars plugin
        File dir = new File(plugin.getDataFolder(), FolderProperties.CHEST_TYPES_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) return;

        // Reset all currently loaded chest types
        this.chests.clear();

        // Load all from directory
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {

            // Sanity checks
            if (file.isDirectory() || !file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            if (getChestTypeByName(name) != null) continue;

            // Load data & store in cache
            SWChestType chestType = this.initChestType(name);
            chests.put(name, chestType);

            chestType.loadData();
            plugin.getLogger().info("Loaded chest type '" + name + "'.");
        }
    }

    @Override
    @SuppressWarnings("unused")
    public void createDefaultsIfNotPresent() {
        File dir = new File(plugin.getDataFolder(), FolderProperties.CHEST_TYPES_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        // List all from directory
        File[] files = dir.listFiles();
        if (files == null) return;

        // Add default files on first install
        if (files.length < 1) {
            this.createChestType("normal");
            this.createChestType("center");
        }
    }

    @Override
    public SWChestType getChestTypeByName(String chestId) {
        return chests.getOrDefault(chestId, null);
    }

    @Override
    public void deleteChestType(String chestId) {
        // todo chest type deletion here
    }

    @Override
    public List<SWChestType> getChestTypes() {
        return new ArrayList<>(chests.values());
    }

    @Override
    public SWChestType createChestType(@NotNull String name) {
        if (getChestTypeByName(name) != null) return null;

        SWChestType chestType = this.initChestType(name);
        chestType.saveData();
        chests.put(name, chestType);
        return chestType;
    }

    // Platform specific

    @Override
    public abstract SWChestType initChestType(String name);

}
