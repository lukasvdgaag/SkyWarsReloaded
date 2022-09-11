package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.chest.tier.LootTableChestTier;
import net.gcnt.skywarsreloaded.game.chest.tier.SimpleChestTier;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoreChestManager implements SWChestManager {

    public final SkyWarsReloaded plugin;
    public HashMap<String, SWChestTier> chests;
    protected File chestFolder;

    public CoreChestManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.chests = new HashMap<>();
        this.chestFolder = new File(plugin.getDataFolder(), FolderProperties.CHEST_TYPES_FOLDER.toString());
    }

    @Override
    public void loadAllChestTiers() {
        // Chests folder under skywars plugin

        // Sanity checks
        if (!chestFolder.exists()) return;

        // Reset all currently loaded chest types
        this.chests.clear();

        // Load all from directory
        this.loadAllChestTypesFromDir(chestFolder, 0, 1, "");
    }

    private void loadAllChestTypesFromDir(File dir, int depth, int maxDepth, String currentPrefix) {
        // Sanity checks
        if (depth > maxDepth) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {

            // Sanity checks
            if (file.isDirectory()) {
                String updatedPrefix = currentPrefix + file.getName() + "/";
                this.loadAllChestTypesFromDir(file, depth + 1, maxDepth, updatedPrefix);
                continue;
            } else if (!file.getName().endsWith(".yml")) continue;

            String fileName = file.getName();
            String name = fileName.replace(".yml", "");
            if (getChestTierByName(name) != null) continue;

            // Load data & store in cache
            this.initChestTier(fileName);
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
            this.createChestTier("normal.yml");
//            this.createChestTier("insane"); // todo re-add insane chest type default
        }
    }

    @Override
    public SWChestTier getChestTierByName(String chestId) {
        return chests.getOrDefault(chestId, null);
    }

    @Override
    public void deleteChestTier(String chestId) {
        // todo chest type deletion here
    }

    @Override
    public List<SWChestTier> getChestTiers() {
        return new ArrayList<>(chests.values());
    }

    @Override
    public SWChestTier createChestTier(@NotNull String name) {
        if (getChestTierByName(name) != null) throw new IllegalArgumentException("Attempted to create chest tier that already exists!");

        SWChestTier chestType = this.initChestTier(name, true);
        chestType.saveData();
        chests.put(name, chestType);
        return chestType;
    }

    // Platform specific

    @Override
    public SWChestTier initChestTier(String name) {
        return initChestTier(name, false);
    }

    @Override
    public SWChestTier initChestTier(String name, boolean generateDefault) {
        final String strippedName = name.replace(".yml", "").replace(".json", "");
        final SWChestTier cached = getChestTierByName(strippedName);
        if (cached != null) return cached;

        if (name.endsWith(".yml") && (new File(chestFolder, strippedName + ".yml").exists() || generateDefault)) {
            final SimpleChestTier tier = new SimpleChestTier(plugin, strippedName);
            chests.put(strippedName, tier);
            return tier;
        } else if (name.endsWith(".json") && (new File(chestFolder, strippedName + ".json").exists() || generateDefault)) {
            final LootTableChestTier tier = new LootTableChestTier(plugin, strippedName);
            chests.put(strippedName, tier);
            return tier;
        }

        plugin.getLogger().error("Count not initialize chest type with name '" + strippedName + "' because it doesn't exist. No .yml or .json file with that name was found in the /chests folder.");
        return null;
    }
}
