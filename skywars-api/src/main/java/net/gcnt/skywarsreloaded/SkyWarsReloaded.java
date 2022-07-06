package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;
import net.gcnt.skywarsreloaded.game.GameManager;
import net.gcnt.skywarsreloaded.game.chest.ChestManager;
import net.gcnt.skywarsreloaded.game.kits.KitManager;
import net.gcnt.skywarsreloaded.game.loader.GameWorldLoader;
import net.gcnt.skywarsreloaded.listener.SWEventListener;
import net.gcnt.skywarsreloaded.manager.*;
import net.gcnt.skywarsreloaded.protocol.NMSManager;
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

    // Configs

    void onEnable();

    void onDisable();

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

    GameManager getGameManager();

    void setGameManager(GameManager gameManager);

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

    // Console

    SWCommandSender getConsoleSender();

    void setConsoleSender(SWCommandSender consoleSender);


    // Player Data

    Storage getStorage();

    void setStorage(Storage storage);

    SWPlayerDataManager getPlayerDataManager();

    void setPlayerDataManager(SWPlayerDataManager playerDataManager);

    PlatformUtils getUtils();

    void setPlatformUtils(PlatformUtils utils);

    ChestManager getChestManager();

    void setChestManager(ChestManager chestManager);

    SWEventListener getEventListener();

    void setEventListener(SWEventListener listener);

    SWScheduler getScheduler();

    void setScheduler(SWScheduler scheduler);


    // Plugin

    SWLogger getLogger();

    void setLogger(SWLogger logger);

    // ---- Util ----

    void disableSkyWars();

    /**
     * Get the plugin version
     *
     * @return The plugin version
     */
    String getVersion();

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
}
