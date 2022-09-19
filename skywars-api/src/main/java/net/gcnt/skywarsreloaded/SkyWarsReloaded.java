package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.data.Storage;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.games.GameInstanceStorage;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.loader.GameWorldLoader;
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

    // Console

    SWCommandSender getConsoleSender();

    void setConsoleSender(SWCommandSender consoleSender);


    // Player Data

    Storage getStorage();

    void setStorage(Storage storage);

    // Instance Data

    GameInstanceStorage getGameInstanceStorage();

    void setGameInstanceStorage(GameInstanceStorage instanceStorage);

    // Listeners

    void setPlatformUtils(PlatformUtils utils);

    SWEventListener getEventListener();

    void setEventListener(SWEventListener listener);

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
