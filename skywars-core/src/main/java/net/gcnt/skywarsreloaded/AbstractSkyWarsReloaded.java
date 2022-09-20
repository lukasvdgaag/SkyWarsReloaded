package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.data.MySQLStorage;
import net.gcnt.skywarsreloaded.data.Storage;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.games.GameInstanceStorage;
import net.gcnt.skywarsreloaded.data.games.MySQLGameInstanceStorage;
import net.gcnt.skywarsreloaded.data.player.SQLitePlayerStorage;
import net.gcnt.skywarsreloaded.game.chest.filler.ChestFillerManager;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.loader.GameWorldLoader;
import net.gcnt.skywarsreloaded.listener.SWEventListener;
import net.gcnt.skywarsreloaded.manager.*;
import net.gcnt.skywarsreloaded.manager.gameinstance.GameInstanceManager;
import net.gcnt.skywarsreloaded.manager.gameinstance.LocalGameInstanceManager;
import net.gcnt.skywarsreloaded.utils.PlatformUtils;
import net.gcnt.skywarsreloaded.utils.SWLogger;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
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
    private NMSManager nmsManager;
    private GameInstanceStorage instanceStorage;

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
    private GameInstanceManager instanceManager;
    private SWPlayerManager playerManager;
    private KitManager kitManager;
    private SWChestManager chestManager;
    private CageManager cageManager;
    private UnlockablesManager unlockablesManager;
    private EntityManager entityManager;
    private SWEventListener eventListener;
    private GameWorldLoader worldLoader;
    private ScoreboardManager scoreboardManager;
    private ItemManager itemManager;
    private SWChestFillerManager chestFillerManager;
    private SWGuiManager guiManager;
    private SWInventoryManager inventoryManager;

    // others
    private SWCommandSender consoleSender;

    /**
     * To inject platform specific code, you can override the preEnable and postEnable methods which
     * are empty by default.
     */
    @Override
    public void onEnable() {
        this.preEnable();

        // Utils
        initLogger();
        initPlatformUtils();
        initScheduler();
        try {
            initNMSManager();
        } catch (IllegalStateException ex) {
            this.getLogger().error(ex.getMessage());
            this.onDisable();
            return;
        }
        initGameInstanceStorage();

        // Data and configs
        initYAMLManager();
        setConfig(getYAMLManager().loadConfig("config", getDataFolder(), "config.yml")); // requires yaml mgr
        setDataConfig(getYAMLManager().loadConfig("data", getDataFolder(), "data.yml")); // requires yaml mgr
        setMessages(getYAMLManager().loadConfig("messages", getDataFolder(), "messages.yml")); // requires yaml mgr

        setStorage(new SQLitePlayerStorage(this)); // requires config
        setupStorage();

        // Managers
        initServer();
        initItemManager();
        initChestFillerManager();
        initPlayerManager();
        initCommandManager();
        initGameInstanceManager();
        initPlayerDataManager();
        initKitManager();
        initChestManager();
        initCageManager();
        getCageManager().loadAllCages();
        initUnlockablesManager();
        initEntityManager();
        initScoreboardManager();
        initGuiManager();
        initInventoryManager();

        setSchematicManager(new CoreSchematicManager(this));

        // Player data
        getPlayerManager().initAllPlayers();
        getPlayerDataManager().loadAllPlayerData();

        // Templates
        getChestManager().createDefaultsIfNotPresent();
        getChestManager().loadAllChestTiers();
        getKitManager().createDefaultsIfNotPresent();
        getKitManager().loadAllKits();
        getGameInstanceManager().loadAllGameTemplates();

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

    private void setupStorage() {
        String username = getConfig().getString(ConfigProperties.STORAGE_USERNAME.toString());
        String password = getConfig().getString(ConfigProperties.STORAGE_PASSWORD.toString());
        int port = getConfig().getInt(ConfigProperties.STORAGE_PORT.toString());
        getStorage().setup(username, password, port);
    }

    @Override
    public void onDisable() {
        GameInstanceManager gameInstanceManager = getGameInstanceManager();

        // Only remove instances if running locally
        if (gameInstanceManager instanceof LocalGameInstanceManager) {
            for (GameInstance gameInstance : gameInstanceManager.getGameInstancesList()) {
                gameInstanceManager.deleteGameInstance(gameInstance);
            }
        }
    }

    // Getters and setters

    @Override
    public SWLogger getLogger() {
        return this.swLogger;
    }

    @Override
    public void setLogger(SWLogger swLoggerIn) {
        this.swLogger = swLoggerIn;
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
    public GameWorldLoader getWorldLoader() {
        return worldLoader;
    }

    @Override
    public void setWorldLoader(GameWorldLoader loader) {
        this.worldLoader = loader;
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
    public SWCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void setCommandManager(SWCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public GameInstanceManager getGameInstanceManager() {
        return this.instanceManager;
    }

    @Override
    public void setGameInstanceManager(GameInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
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
    public CageManager getCageManager() {
        return cageManager;
    }

    @Override
    public void setCageManager(CageManager cageManager) {
        this.cageManager = cageManager;
    }

    @Override
    public UnlockablesManager getUnlockablesManager() {
        return unlockablesManager;
    }

    @Override
    public void setUnlockablesManager(UnlockablesManager unlockablesManager) {
        this.unlockablesManager = unlockablesManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public void setScoreboardManager(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public SWChestFillerManager getChestFillerManager() {
        return chestFillerManager;
    }

    @Override
    public void setChestFillerManager(SWChestFillerManager chestFillerManager) {
        this.chestFillerManager = chestFillerManager;
    }

    @Override
    public ItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public SWGuiManager getGuiManager() {
        return guiManager;
    }

    @Override
    public void setGuiManager(SWGuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public SWInventoryManager getInventoryManager() {
        return inventoryManager;
    }

    @Override
    public void setInventoryManager(SWInventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
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
    public SWChestManager getChestManager() {
        return chestManager;
    }

    @Override
    public void setChestManager(SWChestManager chestManager) {
        this.chestManager = chestManager;
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
    public GameInstanceStorage getGameInstanceStorage() {
        return instanceStorage;
    }

    @Override
    public void setGameInstanceStorage(GameInstanceStorage instanceStorage) {
        this.instanceStorage = instanceStorage;
    }

    @Override
    public void setPlatformUtils(PlatformUtils utils) {
        this.platformUtils = utils;
    }

    @Override
    public SWEventListener getEventListener() {
        return this.eventListener;
    }

    @Override
    public void setEventListener(SWEventListener listener) {
        this.eventListener = listener;
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

    @Override
    public NMSManager getNMSManager() {
        return this.nmsManager;
    }

    @Override
    public void setNMSManager(NMSManager nmsManagerIn) {
        this.nmsManager = nmsManagerIn;
    }

    @Override
    public PlatformUtils getUtils() {
        return this.platformUtils;
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

    protected void initChestManager() {
        this.chestManager = new CoreChestManager(this);
    }

    protected abstract void initCommandManager();

    protected abstract void initCommands();

    protected abstract void initConsoleSender();

    protected abstract void initEventListener();

    protected abstract void initGameInstanceManager();

    protected abstract void initKitManager();

    protected abstract void initCageManager();

    protected abstract void initLogger();

    protected abstract void initNMSManager() throws IllegalStateException;

    protected abstract void initPlatformUtils();

    protected abstract void initPlayerDataManager();

    protected abstract void initPlayerManager();

    protected abstract void initScheduler();

    protected abstract void initServer();

    protected abstract void initWorldLoader();

    protected abstract void initYAMLManager();

    protected abstract void initUnlockablesManager();

    protected abstract void initEntityManager();

    protected abstract void initScoreboardManager();

    protected abstract void initItemManager();

    protected abstract void initInventoryManager();

    // Core initializers

    protected void initGameInstanceStorage() {
        setGameInstanceStorage(new MySQLGameInstanceStorage(this));
    }

    protected void initChestFillerManager() {
        setChestFillerManager(new ChestFillerManager(this));
    }

    protected void initGuiManager() {
        setGuiManager(new CoreGuiManager(this));
    }
}
