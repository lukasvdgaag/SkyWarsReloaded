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
import net.gcnt.skywarsreloaded.listener.SWEventListener;
import net.gcnt.skywarsreloaded.manager.SWPlayerManager;
import net.gcnt.skywarsreloaded.utils.PlatformUtils;
import net.gcnt.skywarsreloaded.utils.SWLogger;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.io.File;

/**
 * The interface for the main class of the SkyWarsReloaded plugin.
 */
public interface SkyWarsReloaded {

    // Configs

    void onEnable();

    File getDataFolder();

    YAMLManager getYAMLManager();

    void setYAMLManager(YAMLManager yamlManager);

    YAMLConfig getConfig();

    void setConfig(YAMLConfig config);

    YAMLConfig getMessages();

    void setMessages(YAMLConfig config);


    // Other plugin managers

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


    // Plugin

    SWLogger getLogger();

    void setLogger(SWLogger logger);

    void initCommands();


    // ---- Util ----

    void disableSkyWars();

    /**
     * Get the plugin version
     * @return The plugin version
     */
    String getVersion();

    /**
     * Get the server's minecraft version
     * @return The server's minecraft version
     */
    String getMinecraftVersion();

    /**
     * Get the platform's version (such as git-Purpur-1413)
     * @return The platform's version
     */
    String getPlatformVersion();
}
