package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.ChestTierProperties;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSWChestTier implements SWChestTier {

    private final SkyWarsReloaded plugin;
    private final String name;
    private final YAMLConfig config;
    private final HashMap<ChestType, HashMap<Integer, List<Item>>> inventoryContents;

    private String displayName;


    public AbstractSWChestTier(SkyWarsReloaded plugin, String nameIn) {
        this.plugin = plugin;
        this.name = nameIn;
        this.inventoryContents = new HashMap<>();

        this.config = plugin.getYAMLManager().loadConfig(
                "chest-" + nameIn,
                FolderProperties.CHEST_TYPES_FOLDER.toString(),
                nameIn + ".yml",
                "/chests/default.yml");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public HashMap<ChestType, HashMap<Integer, List<Item>>> getAllContents() {
        return inventoryContents;
    }

    @Override
    public HashMap<Integer, List<Item>> getContents(ChestType type) {
        inventoryContents.forEach((key, value) -> System.out.println("type found: " + key.getId()));
        return inventoryContents.get(type);
    }

    @Override
    public boolean hasChestType(ChestType chestType) {
        return inventoryContents.containsKey(chestType);
    }

    @Override
    public Item[] generateChestLoot(ChestType type, boolean doubleChest) {

    }

    @Override
    public synchronized void loadData() {
        // Basic chest type info init
        this.displayName = config.getString(ChestTierProperties.DISPLAY_NAME.toString(), name);

        try {
            config.getKeys(ChestTierProperties.TYPES.toString()).stream().map(ChestType::getById)
                    .forEach(this::loadDataFromChestType);

        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to load chest type with id %s. Ignoring it. (%s)", name, e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }

    private void loadDataFromChestType(ChestType chestType) {
        System.out.println("Loading chest type data for type: " + chestType.getId());
        // Init data
        String gameTypeConfigSectionName = chestType.getId();
        String configPath = gameTypeConfigSectionName + ChestTierProperties.CONTENTS;

        // Load inventory content
        HashMap<Integer, List<Item>> gameTypeItems = new HashMap<>();

        if (config.isSet(configPath)) {
            config.getKeys(configPath).forEach(chance -> {
                try {
                    int number = Integer.parseInt(chance);
                    final List<Item> items = config.getItemList(configPath + "." + chance);
                    System.out.println("- Found " + number + " items for chance " + chance + ".");
                    gameTypeItems.put(number, items);
                } catch (Exception e) {
                    plugin.getLogger().error(
                            String.format("Failed to load percentage '%s' under game type '%s' for chest type '%s'. Ignoring it. (%s)",
                                    chance, gameTypeConfigSectionName, name, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }

        // Apply it to the data map
        inventoryContents.put(chestType, gameTypeItems);
    }

    @Override
    public void saveData() {
        config.set(ChestTierProperties.DISPLAY_NAME.toString(), displayName);

        getAllContents().forEach(
                (chestType, contents) -> {
                    String gameTypeConfigSectionName = chestType.getId();
                    contents.forEach((slot, item) ->
                            config.set(gameTypeConfigSectionName + "." + ChestTierProperties.CONTENTS + "." + slot, item));
                }
        );

        config.save();
    }
}
