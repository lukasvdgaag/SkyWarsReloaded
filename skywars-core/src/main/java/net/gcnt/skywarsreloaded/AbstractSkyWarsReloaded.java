package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SQLiteStorage;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.CoreSchematicManager;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;
import net.gcnt.skywarsreloaded.game.GameManager;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.chest.ChestManager;
import net.gcnt.skywarsreloaded.game.kits.KitManager;
import net.gcnt.skywarsreloaded.game.loader.GameWorldLoader;
import net.gcnt.skywarsreloaded.listener.SWEventListener;
import net.gcnt.skywarsreloaded.manager.SWPlayerManager;
import net.gcnt.skywarsreloaded.utils.PlatformUtils;
import net.gcnt.skywarsreloaded.utils.SWLogger;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWScheduler;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.server.SWServer;

/**
 * Abstract SkyWarsReloaded class that needs to be inherited by a subclass.
 */
public abstract class AbstractSkyWarsReloaded implements SkyWarsReloaded {

    // Utils
    private SWLogger swLogger;
    private PlatformUtils platformUtils;
    private SWScheduler scheduler;
    private SWServer server;

    // Data
    private Storage storage;
    private YAMLConfig config;
    private YAMLConfig messages;
    private YAMLConfig dataConfig;

    // Managers
    private YAMLManager yamlManager;
    private SWPlayerDataManager playerDataManager;
    private SchematicManager schematicManager;
    private SWCommandManager commandManager;
    private GameManager gameManager;
    private SWPlayerManager playerManager;
    private KitManager kitManager;
    private ChestManager chestManager;
    private SWEventListener eventListener;
    private GameWorldLoader worldLoader;

    // others
    private SWCommandSender consoleSender;

    /**
     * To inject platform specific code, you can override the preEnable and postEnable methods which
     * are empty by default.
     */
    @Override
    public void onEnable() {
        this.preEnable();

        // utils
        initLogger();
        initPlatformUtils();
        initScheduler();

        // Data and configs
        initYAMLManager();
        setConfig(getYAMLManager().loadConfig("config", getDataFolder(), "config.yml")); // requires yaml mgr
        setDataConfig(getYAMLManager().loadConfig("data", getDataFolder(), "data.yml")); // requires yaml mgr
        setMessages(getYAMLManager().loadConfig("messages", getDataFolder(), "messages.yml")); // requires yaml mgr

        setStorage(new SQLiteStorage(this)); // requires config

        // Managers
        initPlayerManager();
        initCommandManager();
        initGameManager();
        initPlayerDataManager();
        initKitManager();
        initChestManager();
        setSchematicManager(new CoreSchematicManager(this));

        // Player data

        // Templates
        getGameManager().loadAllGameTemplates();
        getKitManager().createDefaultsIfNotPresent();
        getKitManager().loadAllKits();
        getChestManager().createDefaultsIfNotPresent();
        getChestManager().loadAllChestTypes();

        // Worlds
        initWorldLoader();

        // Console
        initConsoleSender();

        // Commands
        getCommandManager().registerBaseCommands();
        getCommandManager().registerMapCommands();
        getCommandManager().registerKitCommands();
        initCommands(); // Register commands to the platform

        // Events - Init objects and register event listener to the platform
        initEventListener();

        // Plugin messaging
        // todo

        this.postEnable();
    }

    @Override
    public void onDisable() {
        for (GameWorld world : getGameManager().getGameWorlds()) {
            getWorldLoader().deleteWorldInstance(world);
        }
    }

    // Initialization

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


    public abstract void initLogger();

    protected abstract void initScheduler();

    public abstract void initPlatformUtils();

    public abstract void initYAMLManager();

    public abstract void initPlayerManager();

    public abstract void initPlayerDataManager();

    public abstract void initCommandManager();

    public abstract void initConsoleSender();

    public abstract void initKitManager();

    public abstract void initChestManager();

    public abstract void initEventListener();

    public abstract void initWorldLoader();

    public abstract void initGameManager();

    public abstract void initServer();

    // Getters & Setters

    @Override
    public SWLogger getLogger() {
        return this.swLogger;
    }

    @Override
    public void setLogger(SWLogger swLoggerIn) {
        this.swLogger = swLoggerIn;
    }

    @Override
    public SchematicManager getSchematicManager() {
        return this.schematicManager;
    }

    @Override
    public void setSchematicManager(SchematicManager schematicManager) {
        this.schematicManager = schematicManager;
    }

    @Override
    public KitManager getKitManager() {
        return kitManager;
    }

    @Override
    public void setKitManager(KitManager kitManager) {
        this.kitManager = kitManager;
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
    public YAMLConfig getDataConfig() {
        return this.dataConfig;
    }

    @Override
    public void setDataConfig(YAMLConfig data) {
        this.dataConfig = data;
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
    public PlatformUtils getUtils() {
        return this.platformUtils;
    }

    @Override
    public void setPlatformUtils(PlatformUtils utils) {
        this.platformUtils = utils;
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
    public SWPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public void setPlayerManager(SWPlayerManager playerManagerIn) {
        this.playerManager = playerManagerIn;
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

    @Override
    public ChestManager getChestManager() {
        return chestManager;
    }

    @Override
    public void setChestManager(ChestManager chestManager) {
        this.chestManager = chestManager;
    }

    @Override
    public SWEventListener getEventListener() {
        return this.eventListener;
    }

    @Override
    public void setEventListener(SWEventListener listener) {
        this.eventListener = listener;
    }

    @Override
    public GameWorldLoader getWorldLoader() {
        return worldLoader;
    }

    @Override
    public void setWorldLoader(GameWorldLoader loader) {
        this.worldLoader = loader;
    }

    public SWScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(SWScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public SWServer getServer() {
        return this.server;
    }

    @Override
    public void setServer(SWServer serverIn) {
        this.server = serverIn;
    }
}
