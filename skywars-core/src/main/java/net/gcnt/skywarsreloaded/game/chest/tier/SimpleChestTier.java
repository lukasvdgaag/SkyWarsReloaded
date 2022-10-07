package net.gcnt.skywarsreloaded.game.chest.tier;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.chest.filler.ChestFillerManager;
import net.gcnt.skywarsreloaded.game.chest.filler.SWChestFiller;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.ChestTierProperties;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SimpleChestTier extends AbstractSWChestTier {

    private final YAMLConfig config;
    private final HashMap<ChestType, HashMap<Integer, List<Item>>> inventoryContents;

    public SimpleChestTier(SkyWarsReloaded plugin, String name) {
        super(plugin, name);
        this.inventoryContents = new HashMap<>();

        this.config = plugin.getYAMLManager().loadConfig("chest-" + name, FolderProperties.CHEST_TYPES_FOLDER.toString(), name + ".yml", "/chests/normal.yml");
        loadData();
    }

    public HashMap<ChestType, HashMap<Integer, List<Item>>> getAllContents() {
        return inventoryContents;
    }

    public HashMap<Integer, List<Item>> getContents(ChestType type) {
        return inventoryContents.get(type);
    }

    @Override
    public synchronized void loadData() {
        // Basic chest type info init
        this.displayName = config.getString(ChestTierProperties.DISPLAY_NAME.toString(), name);

        try {
            final Set<String> keys = config.getKeys(ChestTierProperties.TYPES.toString());
            if (keys == null) {
                plugin.getLogger().error("No chest types found in chest tier " + name);
            } else {
                keys.stream().map(ChestType::getById).forEach(this::loadDataFromChestType);
            }
        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to load chest type with id %s. Ignoring it. (%s)", name, e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }

    private void loadDataFromChestType(ChestType chestType) {
        plugin.getLogger().debug("Loading chest type data for type: " + chestType.getId());
        // Init data
        String gameTypeConfigSectionName = chestType.getId();
        String configPath = ChestTierProperties.TYPES + "." + gameTypeConfigSectionName + "." + ChestTierProperties.CONTENTS;

        // Load inventory content
        HashMap<Integer, List<Item>> gameTypeItems = new HashMap<>();

        plugin.getLogger().debug(configPath);
        if (config.isSet(configPath)) {
            final Set<String> keys = config.getKeys(configPath);
            if (keys == null) return;
            keys.forEach(chance -> {
                try {
                    int number = Integer.parseInt(chance);
                    final List<Item> items = config.getItemList(configPath + "." + chance);
                    plugin.getLogger().debug("- Found " + items.size() + " items for chance " + chance + ".");
                    gameTypeItems.put(number, items);
                } catch (Exception e) {
                    plugin.getLogger().error(String.format("Failed to load percentage '%s' under game type '%s' for chest type '%s'. Ignoring it. (%s)", chance, gameTypeConfigSectionName, name, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }

        // Apply it to the data map
        inventoryContents.put(chestType, gameTypeItems);
    }

    @Override
    public void saveData() {
        config.set(ChestTierProperties.DISPLAY_NAME.toString(), displayName);

        getAllContents().forEach((chestType, contents) -> {
            if (chestType == null || contents == null) return;
            String gameTypeConfigSectionName = chestType.getId();
            contents.forEach((slot, item) -> config.set(gameTypeConfigSectionName + "." + ChestTierProperties.CONTENTS + "." + slot, item));
        });

        config.save();
    }

    @Override
    public boolean hasChestType(ChestType chestType) {
        return inventoryContents.containsKey(chestType);
    }

    @Override
    public SWChestFiller getChestFiller() {
        return plugin.getChestFillerManager().getFillerByName(ChestFillerManager.SIMPLE);
    }
}
