package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.games.GameInstanceStorage;
import net.gcnt.skywarsreloaded.data.messaging.SWMessaging;
import net.gcnt.skywarsreloaded.data.player.SWPlayerStorage;
import net.gcnt.skywarsreloaded.data.redis.SWRedisConnection;
import net.gcnt.skywarsreloaded.data.sql.SQLStorage;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.loader.GameWorldLoader;
import net.gcnt.skywarsreloaded.hook.SWHook;
import net.gcnt.skywarsreloaded.listener.SWEventListener;
import net.gcnt.skywarsreloaded.manager.*;
import net.gcnt.skywarsreloaded.manager.gameinstance.GameInstanceManager;
import net.gcnt.skywarsreloaded.utils.PlatformUtils;
import net.gcnt.skywarsreloaded.utils.SWLogger;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWScheduler;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.server.SWServer;

import java.io.File;

/**
 * The interface for the main class of the SkyWarsReloaded plugin.
 */
public interface SkyWarsReloaded {

    // Plugin

    void onEnable();

    void onDisable();

    /**
     * Registers the default hooks that SkyWars uses for compatibility
     * This method can be overridden if you wish to disable the default SkyWars hooks.
     * <br><br>
     * If you want to register plugin hooks into SkyWars that can be hooked after the {@link #onEnable()}
     * is executed, we recommend using {@link #getHookManager()} then {@link SWHookManager#registerHook(SWHook)}
     * <br><br>
     * If you really need to register your own hooks into SkyWars that will run at enable time, we recommend
     * overriding this method in your own SkyWars implementation and calling super.{@link #registerDefaultHooks()}
     * before registering your own.
     */
    void registerDefaultHooks();

    SWLogger getLogger();

    void setLogger(SWLogger logger);

    void disableSkyWars();

    /**
     * Get the plugin version
     *
     * @return The plugin version
     */
    String getVersion();

    // Config and init data

    File getDataFolder();

    YAMLManager getYAMLManager();

    void setYAMLManager(YAMLManager yamlManager);

    YAMLConfig getConfig();

    void setConfig(YAMLConfig config);

    YAMLConfig getDataConfig();

    void setDataConfig(YAMLConfig data);

    YAMLConfig getMessages();

    void setMessages(YAMLConfig config);

    // Hooks

    SWHookManager getHookManager();

    void setHookManager(SWHookManager hookManager);

    // Other plugin managers

    GameWorldLoader getWorldLoader();

    void setWorldLoader(GameWorldLoader loader);

    SchematicManager getSchematicManager();

    void setSchematicManager(SchematicManager schematicManager);

    KitManager getKitManager();

    void setKitManager(KitManager kitManager);

    SWCommandManager getCommandManager();

    void setCommandManager(SWCommandManager commandManager);

    GameInstanceManager<? extends GameInstance> getGameInstanceManager();

    void setGameInstanceManager(GameInstanceManager<? extends GameInstance> gameManager);

    SWPlayerManager getPlayerManager();

    void setPlayerManager(SWPlayerManager playerManager);

    CageManager getCageManager();

    void setCageManager(CageManager cageManager);

    UnlockablesManager getUnlockablesManager();

    void setUnlockablesManager(UnlockablesManager unlockablesManager);

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    ScoreboardManager getScoreboardManager();

    void setScoreboardManager(ScoreboardManager scoreboardManager);

    SWChestFillerManager getChestFillerManager();

    void setChestFillerManager(SWChestFillerManager chestFillerManager);

    ItemManager getItemManager();

    void setItemManager(ItemManager itemManager);

    SWGuiManager getGuiManager();

    void setGuiManager(SWGuiManager itemManager);

    SWInventoryManager getInventoryManager();

    void setInventoryManager(SWInventoryManager inventoryManager);

    SWPlayerDataManager getPlayerDataManager();

    void setPlayerDataManager(SWPlayerDataManager playerDataManager);

    SWChestManager getChestManager();

    void setChestManager(SWChestManager chestManager);

    SWMessaging getMessaging();

    void setMessaging(SWMessaging messaging);

    // Console

    SWCommandSender getConsoleSender();

    void setConsoleSender(SWCommandSender consoleSender);


    // Player Data

    SQLStorage getMySQLStorage();

    void setMySQLStorage(SQLStorage storage);

    SQLStorage getSQLiteStorage();

    void setSQLiteStorage(SQLStorage storage);

    SWRedisConnection getRedisConnection();

    void setRedisConnection(SWRedisConnection redisConnection);

    SWPlayerStorage getPlayerStorage();

    void setPlayerStorage(SWPlayerStorage storage);

    // Instance Data

    GameInstanceStorage getGameInstanceStorage();

    void setGameInstanceStorage(GameInstanceStorage instanceStorage);

    // Events

    void setEventManager(SWEventManager eventManager);

    SWEventManager getEventManager();

    // Listeners

    void setPlatformUtils(PlatformUtils utils);

    SWEventListener<?> getEventListener();

    void setEventListener(SWEventListener<?> listener);

    // Messaging

    // Schedulers

    SWScheduler getScheduler();

    void setScheduler(SWScheduler scheduler);

    // Server

    /**
     * Get the server's minecraft version
     *
     * @return The server's minecraft version
     */
    String getMinecraftVersion();

    /**
     * Get the platform's version (such as git-Purpur-1413)
     *
     * @return The platform's version
     */
    String getPlatformVersion();

    /**
     * Get the server wrapper
     *
     * @return The server wrapper
     */
    SWServer getServer();

    /**
     * Set server wrapper
     *
     * @param server The server wrapper
     */
    void setServer(SWServer server);

    /**
     * Get the NMS manager
     *
     * @return The NMS manager
     */
    NMSManager getNMSManager();

    /**
     * Set the NMS manager
     *
     * @param nmsManager The NMS manager
     */
    void setNMSManager(NMSManager nmsManager);

    // Utils

    PlatformUtils getUtils();
}
