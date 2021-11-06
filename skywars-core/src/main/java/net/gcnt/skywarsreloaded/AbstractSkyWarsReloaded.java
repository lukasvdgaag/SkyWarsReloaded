package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SQLiteStorage;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.CoreSchematicManager;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;
import net.gcnt.skywarsreloaded.game.CoreGameManager;
import net.gcnt.skywarsreloaded.game.GameManager;
import net.gcnt.skywarsreloaded.utils.Utilities;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

/**
 * Abstract SkyWarsReloaded class that needs to be inherited by a subclass.
 */
public abstract class AbstractSkyWarsReloaded implements SkyWarsReloaded {

    // Data
    private Storage storage;
    private YAMLConfig config;
    private YAMLConfig messages;

    // Managers
    private YAMLManager yamlManager;
    private SWPlayerDataManager playerDataManager;
    private SchematicManager schematicManager;
    private SWCommandManager commandManager;
    private GameManager gameManager;

    // others
    private Utilities utilities;
    private SWCommandSender consoleSender;

    // Initialization

    @Override
    /**
     * To inject platform specific code, you can override the preEnable and postEnable methods which
     * are empty by default.
     */
    public void onEnable() {
        this.preEnable();

        // utils
        initUtilities();

        // Data and configs
        initYAMLManager();
        setConfig(getYAMLManager().loadConfig("config", getDataFolder(), "config.yml")); // requires yaml mgr
        setMessages(getYAMLManager().loadConfig("messages", getDataFolder(), "messages.yml")); // requires yaml mgr

        setStorage(new SQLiteStorage(this)); // requires config

        // Managers
        initCommandManager();
        setGameManager(new CoreGameManager(this));

        // Player data
        initPlayerDataManager();
        setSchematicManager(new CoreSchematicManager());

        // Templates
        getGameManager().loadAllGameTemplates();

        // Worlds
        //todo

        // Console
        initConsoleSender();

        // Commands
        getCommandManager().registerBaseCommands();

        // Events

        // Plugin messaging

        this.postEnable();
    }

    /**
     * Override to run required initialization before core enable
     */
    public void preEnable() {
    }

    /**
     * Override to run required initialization after core enable
     */
    public void postEnable() {
    }

    public abstract void initUtilities();

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
    public YAMLConfig getMessages() {
        return this.messages;
    }

    @Override
    public void setMessages(YAMLConfig config) {
        this.messages = config;
    }

    @Override
    public SWCommandSender getConsoleSender() {
        return consoleSender;
    }

    @Override
    public void setConsoleSender(SWCommandSender consoleSender) {
        this.consoleSender = consoleSender;
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
    public Utilities getUtils() {
        return this.utilities;
    }

    @Override
    public void setUtils(Utilities utils) {
        this.utilities = utils;
    }

    @Override
    public GameManager getGameManager() {
        return this.gameManager;
    }

    @Override
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
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
