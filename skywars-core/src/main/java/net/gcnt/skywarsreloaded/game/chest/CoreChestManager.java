package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.filler.LootTableChestFiller;
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

    public CoreChestManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.chests = new HashMap<>();
    }

    @Override
    public void loadAllChestTiers() {
        // Chests folder under skywars plugin
        File dir = new File(plugin.getDataFolder(), FolderProperties.CHEST_TYPES_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) return;

        // Reset all currently loaded chest types
        this.chests.clear();

        // Load all from directory
        this.loadAllChestTypesFromDir(dir, 0, 1, "");
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

            String name = file.getName().replace(".yml", "");
            if (getChestTierByName(name) != null) continue;

            // Load data & store in cache
            SWChestTier chestType = this.initChestTier(name);
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
            this.createChestTier("default");
            this.createChestTier("insane");
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
        if (getChestTierByName(name) != null) return null;

        SWChestTier chestType = this.initChestTier(name);
        chestType.saveData();
        chests.put(name, chestType);
        return chestType;
    }

    // Platform specific

    @Override
    public SWChestTier initChestTier(String name) {
        final SWChestTier cached = getChestTierByName(name);
        if (cached != null) return cached;

        if (new File(plugin.getDataFolder(), "chests" + File.separator + name + ".yml").exists()) {
            return new SimpleChestTier(plugin, name);
        } else if (new File(plugin.getDataFolder(), "chests" + File.separator + name + ".json").exists()) {
            return new LootTableChestTier(plugin, name);
        }

        plugin.getLogger().error("Count not initialize chest type with name '" + name + "' because it doesn't exist. No .yml or .json file with that name was found in the /chests folder.");
        return null;
    }

}
