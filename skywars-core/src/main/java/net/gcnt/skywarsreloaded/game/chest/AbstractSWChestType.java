package net.gcnt.skywarsreloaded.game.chest;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.types.GameDifficulty;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.ChestProperties;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.util.HashMap;

public abstract class AbstractSWChestType implements SWChestType {

    private final SkyWarsReloaded plugin;
    private final String name;
    private final YAMLConfig config;

    private String displayName;

    private HashMap<GameDifficulty, HashMap<Integer, Item>> inventoryContents;

    public AbstractSWChestType(SkyWarsReloaded plugin, String nameIn) {
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
    public HashMap<GameDifficulty, HashMap<Integer, Item>> getAllContents() {
        return inventoryContents;
    }

    @Override
    public synchronized void loadData() {
        // Basic chest type info init
        this.displayName = config.getString(ChestProperties.DISPLAY_NAME.toString(), name);

        config.getKeys(ChestProperties.DIFFICULTIES.toString()).stream().map(GameDifficulty::getById)
                .forEach(this::loadDataFromGameType);

    }

    private void loadDataFromGameType(GameDifficulty gameDifficulty) {
        // Init data
        String gameTypeConfigSectionName = gameDifficulty.getId();
        String configPath = gameTypeConfigSectionName + ChestProperties.CONTENTS;

        // Load inventory content
        HashMap<Integer, Item> gameTypeItems = new HashMap<>();

        if (config.isSet(configPath)) {
            config.getKeys(configPath).forEach(slot1 -> {
                try {
                    int number = Integer.parseInt(slot1);
                    gameTypeItems.put(number, config.getItem(configPath + "." + number));
                } catch (Exception e) {
                    plugin.getLogger().error(
                            String.format("Failed to load slot '%s' under game type '%s' for chest type '%s'. Ignoring it. (%s)",
                                    slot1, gameTypeConfigSectionName, name, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }

        // Apply it to the data map
        inventoryContents.put(gameDifficulty, gameTypeItems);
    }

    @Override
    public void saveData() {
        config.set(ChestProperties.DISPLAY_NAME.toString(), displayName);

        getAllContents().forEach(
                (gameDifficulty, contents) -> {
                    String gameTypeConfigSectionName = gameDifficulty.getId();
                    contents.forEach((slot, item) ->
                            config.set(gameTypeConfigSectionName + "." + ChestProperties.CONTENTS + "." + slot, item));
                }
        );

        config.save();
    }
}
