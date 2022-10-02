package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractKitManager implements KitManager {

    public static Integer[] LAYOUT_FILLED;
    public static Integer[] LAYOUT_OUTLINED;

    static {
        LAYOUT_FILLED = new Integer[54];
        LAYOUT_OUTLINED = new Integer[28];
        for (int i = 0; i < 54; i++) {
            LAYOUT_FILLED[i] = i;
            if (i > 9 && i < 45 && i % 9 != 0 && i % 9 != 8) {
                LAYOUT_OUTLINED[i] = i;
            }
        }
    }

    public final SkyWarsReloaded plugin;
    public HashMap<String, SWKit> kits;

    public AbstractKitManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.kits = new HashMap<>();
    }

    @Override
    public SWKit getKitByName(String kitId) {
        return kits.getOrDefault(kitId, null);
    }

    @Override
    public void deleteKit(String kitId) {
        // todo kit deletion here
    }

    @Override
    public void loadAllKits() {
        File dir = new File(plugin.getDataFolder(), FolderProperties.KITS_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) return;

        // Reset all currently loaded kits
        this.kits.clear();

        // Load all from directory
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {

            // Sanity checks
            if (file.isDirectory() || !file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            if (getKitByName(name) != null) continue;

            // Load data & store in cache
            SWKit kit = initKit(name);
            kits.put(name, kit);

            kit.loadData();
            plugin.getLogger().info("Loaded kit '" + name + "'.");
        }

        fixKitSlots();
    }

    @Override
    public List<SWKit> getKits() {
        return new ArrayList<>(kits.values());
    }

    @Override
    public SWKit getKitFromSlot(int slot) {
        for (SWKit kit : kits.values()) {
            if (kit.getSlot() == slot) return kit;
        }
        return null;
    }

    @Override
    public void createDefaultsIfNotPresent() {
        File dir = new File(plugin.getDataFolder(), FolderProperties.KITS_FOLDER.toString());

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
            this.createKit("default", null);
        }
    }

    @Override
    public SWKit createKit(String id, @Nullable Item icon) {
        if (getKitByName(id) != null) return null;
        SWKit kit = initKit(id);
        if (icon != null) kit.setIcon(icon);
        kit.saveData();
        kits.put(id, kit);
        return kit;
    }

    @Override
    public abstract SWKit initKit(String id);

    @Override
    public void fixKitSlots() {
        List<Integer> registeredSlots = new ArrayList<>();
        List<SWKit> duplicates = new ArrayList<>();

        boolean useAutoLayout = plugin.getConfig().getBoolean(ConfigProperties.MENUS_KITS_AUTO_LAYOUT.toString());
        final String layoutType = plugin.getConfig().getString(ConfigProperties.MENUS_KITS_LAYOUT.toString(), "OUTLINED");
        final Integer[] layout = layoutType.equalsIgnoreCase("FILLED") ? LAYOUT_FILLED : LAYOUT_OUTLINED;

        for (SWKit kit : kits.values()) {
            int slot = kit.getSlot();
            if (useAutoLayout || slot < 0 || slot >= 54 || registeredSlots.contains(kit.getSlot())) {
                duplicates.add(kit);
            } else {
                registeredSlots.add(slot);
            }
        }

        for (SWKit kit : duplicates) {
            int emptySlot = getEmptySlot(registeredSlots, layout);
            // kit slot will be set to -1 when invalid or anything else when valid.
            // kits with -1 will be ignored later on.
            kit.setSlot(emptySlot);
            if (emptySlot < 0) {
                plugin.getLogger().error("No more empty slots could be found for the selected kit menu layout. '" + kit.getId() + "'.");
                break;
            }
            kit.saveData();
        }

    }

    private int getEmptySlot(List<Integer> registeredSlots, Integer[] layout) {
        for (int slot : layout) {
            if (!registeredSlots.contains(slot)) {
                return slot;
            }
        }
        return -1;
    }

}
