package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.command.CoreSWCommandManager;
import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SQLiteStorage;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.CoreSchematicManager;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;

/**
 * Abstract SkyWarsReloaded class that needs to be inherited by a subclass.
 */
public abstract class AbstractSkyWarsReloaded implements SkyWarsReloaded {

    // Data
    private Storage storage;
    private YAMLConfig config;

    // Managers
    private YAMLManager yamlManager;
    private SWPlayerDataManager playerDataManager;
    private SchematicManager schematicManager;
    private SWCommandManager commandManager;

    // Initialization

    @Override
    /**
     * To inject platform specific code, you can override the preEnable and postEnable methods which
     * are empty by default.
     */
    public void onEnable() {
        this.preEnable();

        // Data and configs
        initYAMLManager();
        setConfig(getYAMLManager().loadConfig("config", getDataFolder(), "config.yml")); // requires yaml mgr
        setStorage(new SQLiteStorage(this)); // requires config

        // Managers
        initCommandManager();

        // Player data
        initPlayerDataManager();
        setSchematicManager(new CoreSchematicManager());

        // Commands
        getCommandManager().registerBaseCommands();

        // Events

        // Plugin messaging

        this.postEnable();
    }

    /**
     * Override to run required initialization before core enable
     */
    public void preEnable() {}

    /**
     * Override to run required initialization after core enable
     */
    public void postEnable() {}

    public abstract void initYAMLManager();

    public abstract void initPlayerDataManager();

    public abstract void initCommandManager();


    // Getters & Setters

    @Override
    public SchematicManager getSchematicManager() {
        return this.schematicManager;
    }

    @Override
    public void setSchematicManager(SchematicManager schematicManager) {
        this.schematicManager = schematicManager;
    }

    @Override
    public YAMLConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(YAMLConfig config) {
        this.config = config;
    }

    @Override
    public Storage getStorage() {
        return this.storage;
    }

    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public SWPlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    @Override
    public void setPlayerDataManager(SWPlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Override
    public YAMLManager getYAMLManager() {
        return this.yamlManager;
    }

    @Override
    public void setYAMLManager(YAMLManager yamlManager) {
        this.yamlManager = yamlManager;
    }

    @Override
    public SWCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void setCommandManager(SWCommandManager commandManager) {
        this.commandManager = commandManager;
    }
}
