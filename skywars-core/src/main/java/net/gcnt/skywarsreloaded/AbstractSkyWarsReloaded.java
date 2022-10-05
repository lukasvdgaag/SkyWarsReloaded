package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.games.GameInstanceStorage;
import net.gcnt.skywarsreloaded.data.player.SWPlayerStorage;
import net.gcnt.skywarsreloaded.data.redis.RedisGameInstanceStorage;
import net.gcnt.skywarsreloaded.data.redis.SWRedisConnection;
import net.gcnt.skywarsreloaded.data.sql.CoreMySQLStorage;
import net.gcnt.skywarsreloaded.data.sql.CoreSQLiteStorage;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.data.sql.tables.SQLGameInstanceTable;
import net.gcnt.skywarsreloaded.data.sql.tables.SQLPlayerTable;
import net.gcnt.skywarsreloaded.data.sql.tables.sqlite.SQLitePlayerTable;
import net.gcnt.skywarsreloaded.game.chest.filler.ChestFillerManager;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.loader.GameWorldLoader;
import net.gcnt.skywarsreloaded.listener.SWEventListener;
import net.gcnt.skywarsreloaded.listener.minecraft.*;
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
    private SWPlayerStorage playerStorage;
    private YAMLConfig config;
    private YAMLConfig messages;
    private YAMLConfig dataConfig;

    // Hooks
    private SWHookManager hookManager;

    // storage
    private SQLStorage mysqlStorage;
    private SQLStorage sqliteStorage;
    private SWRedisConnection redisConnection;

    // Managers
    private YAMLManager yamlManager;
    private SWPlayerDataManager playerDataManager;
    private SchematicManager schematicManager;
    private SWCommandManager commandManager;
    private GameInstanceManager<? extends GameInstance> instanceManager;
    private SWPlayerManager playerManager;
    private KitManager kitManager;
    private SWChestManager chestManager;
    private CageManager cageManager;
    private UnlockablesManager unlockablesManager;
    private EntityManager entityManager;
    private SWEventListener<?> eventListener;
    private GameWorldLoader worldLoader;
    private ScoreboardManager scoreboardManager;
    private ItemManager itemManager;
    private SWChestFillerManager chestFillerManager;
    private SWGuiManager guiManager;
    private SWInventoryManager inventoryManager;
    private SWEventManager eventManager;

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

        // Data and configs
        initYAMLManager();
        setConfig(getYAMLManager().loadConfig("config", getDataFolder(), "config.yml")); // requires yaml mgr
        setDataConfig(getYAMLManager().loadConfig("data", getDataFolder(), "data.yml")); // requires yaml mgr
        setMessages(getYAMLManager().loadConfig("messages", getDataFolder(), "messages.yml")); // requires yaml mgr

        // Storages
        initSQLStorage();

        initGameInstanceStorage();
        initPlayerStorage(); // requires config

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
        initHookManager();

        setSchematicManager(new CoreSchematicManager(this));

        // Hooks
        registerDefaultHooks();
        getHookManager().enableAllHooks();

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

        // Events
        initEventManager();
        initSWEventListeners();
        initPlatformEventListeners(); // Registers event listeners for the platform specified

        // Plugin messaging
        // todo

        this.postEnable();
    }

    @Override
    public void onDisable() {
        // Only remove instances if running locally
        if (getGameInstanceManager() instanceof LocalGameInstanceManager) {
            LocalGameInstanceManager gameInstanceManager = (LocalGameInstanceManager) getGameInstanceManager();

            for (LocalGameInstance gameInstance : gameInstanceManager.getGameInstancesList()) {
                gameInstanceManager.deleteGameInstance(gameInstance);
            }
        }
    }

    @Override
    public void registerDefaultHooks() {
        // todo
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
    public SWHookManager getHookManager() {
        return this.hookManager;
    }

    @Override
    public void setHookManager(SWHookManager hookManager) {
        this.hookManager = hookManager;
    }

    @Override
    public GameWorldLoader getWorldLoader() {
        return this.worldLoader;
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
    public GameInstanceManager<? extends GameInstance> getGameInstanceManager() {
        return this.instanceManager;
    }

    @Override
    public void setGameInstanceManager(GameInstanceManager<? extends GameInstance> instanceManager) {
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
    public SWPlayerStorage getPlayerStorage() {
        return this.playerStorage;
    }

    @Override
    public void setPlayerStorage(SWPlayerStorage storage) {
        this.playerStorage = storage;
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
    public void setEventManager(SWEventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public SWEventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public void setPlatformUtils(PlatformUtils utils) {
        this.platformUtils = utils;
    }

    @Override
    public SWEventListener<?> getEventListener() {
        return this.eventListener;
    }

    @Override
    public void setEventListener(SWEventListener<?> listener) {
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

    @Override
    public SWRedisConnection getRedisConnection() {
        return redisConnection;
    }

    @Override
    public void setRedisConnection(SWRedisConnection redisConnection) {
        this.redisConnection = redisConnection;
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

    protected abstract void initPlatformEventListeners();

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

    // Core initializers

    protected abstract void initInventoryManager();

    protected void initGameInstanceStorage() {
        if (getConfig().getString(ConfigProperties.MESSAGING_TYPE.toString()).equalsIgnoreCase("Redis")) {
            setGameInstanceStorage(new RedisGameInstanceStorage(this, getRedisConnection()));
        } else {
            setGameInstanceStorage(new SQLGameInstanceTable(getMySQLStorage()));
        }
    }

    protected void initPlayerStorage() {
        // selecting the right storage type based on the config.
        if (getConfig().getString(ConfigProperties.STORAGE_TYPE.toString()).equalsIgnoreCase("MySQL")) {
            setPlayerStorage(new SQLPlayerTable(getMySQLStorage()));
        } else {
            setPlayerStorage(new SQLitePlayerTable(getSQLiteStorage()));
        }
    }

    protected void initChestFillerManager() {
        setChestFillerManager(new ChestFillerManager(this));
    }

    protected void initGuiManager() {
        setGuiManager(new CoreGuiManager(this));
    }

    protected void initHookManager() {
        setHookManager(new CoreSWHookManager(this));
    }

    private void initEventManager() {
        setEventManager(new CoreSWEventManager(this));
    }

    protected void initSQLStorage() {
        // enabling SQLite only if is enabled in the config.
        // enabling MySQL only if is enabled in the config.
        if (getConfig().getString(ConfigProperties.STORAGE_TYPE.toString()).equalsIgnoreCase("MySQL")) {
            setMySQLStorage(new CoreMySQLStorage(this));
        } else {
            // using SQLite as default option
            setSQLiteStorage(new CoreSQLiteStorage(this));
        }
    }

    protected void initMessaging() {
        // enabling SQLite only if is enabled in the config.
        if (getConfig().getString(ConfigProperties.MESSAGING_TYPE.toString()).equalsIgnoreCase("Redis")) {
            setSQLiteStorage(new CoreSQLiteStorage(this));
        } else { // todo this
//            setMessaging(new RedisGameInstanceStorage(this, getRedisConnection()));
        }
    }

    protected void initSWEventListeners() {
        new CoreSWBlockBreakListener(this);
        new CoreSWBlockPlaceListener(this);
        new CoreSWDeathListener(this);
        new CoreSWEntityDamageListener(this);
        new CoreSWEntityDamageByEntityListener(this);
        new CoreSWFoodLevelChangeListener(this);
        new CoreSWInteractionListener(this);
        new CoreSWJoinListener(this);
        new CoreSWMoveListener(this);
        new CoreSWPreLoginListener(this);
        new CoreSWQuitListener(this);
        new CoreSWWorldInitListener(this);
    }

}
