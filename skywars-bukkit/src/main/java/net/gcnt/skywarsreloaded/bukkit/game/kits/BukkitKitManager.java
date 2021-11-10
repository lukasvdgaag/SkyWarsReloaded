package net.gcnt.skywarsreloaded.bukkit.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.kits.AbstractKitManager;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.io.File;

public class BukkitKitManager extends AbstractKitManager {

    public BukkitKitManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public void loadAllKits() {
        File dir = new File(plugin.getDataFolder(), FolderProperties.KITS_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) return;

        // Reset all currently loaded kits
        this.kits.clear();

        // Load all from directory
        for (File file : dir.listFiles()) {

            // Sanity checks
            if (file.isDirectory() || !file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            if (getKitByName(name) != null) continue;

            // Load data & store in cache
            SWKit kit = new BukkitSWKit(plugin, name);
            kits.put(name, kit);

            kit.loadData();
            plugin.getLogger().info("Loaded kit '" + name + "'.");
        }
    }

    @Override
    public SWKit createKit(String id) {
        if (getKitByName(id) != null) return null;
        SWKit kit = new BukkitSWKit(plugin, id);
        kit.saveData();
        kits.put(id, kit);
        return kit;
    }
}