package net.gcnt.skywarsreloaded.bukkit.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.kits.BukkitSWKit;
import net.gcnt.skywarsreloaded.game.chest.AbstractChestManager;
import net.gcnt.skywarsreloaded.game.chest.SWChestType;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.io.File;

public class BukkitChestManager extends AbstractChestManager {

    public BukkitChestManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public void loadAllChestTypes() {
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
            SWChestType chestType = new BukkitSWChestType(plugin, name);
            chests.put(name, chestType);

            chestType.loadData();
            plugin.getLogger().info("Loaded chest type '" + name + "'.");
        }
    }

    @Override
    public SWChestType createChestType(String id) {
        if (getChestTypeByName(id) != null) return null;
        SWChestType chestType = new BukkitSWChestType(plugin, id);
        chestType.saveData();
        chests.put(id, chestType);
        return chestType;
    }
}