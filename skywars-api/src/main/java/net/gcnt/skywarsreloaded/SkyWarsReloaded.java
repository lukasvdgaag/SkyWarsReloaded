package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;
import net.gcnt.skywarsreloaded.game.GameManager;
import net.gcnt.skywarsreloaded.utils.Utilities;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.io.File;
import java.util.logging.Logger;

/**
 * The interface for the main class of the SkyWarsReloaded plugin.
 */
public interface SkyWarsReloaded {

    // ---- Getters ----

    // Configs

    void onEnable();

    File getDataFolder();

    YAMLManager getYAMLManager();

    void setYAMLManager(YAMLManager yamlManager);

    YAMLConfig getConfig();

    void setConfig(YAMLConfig config);

    YAMLConfig getMessages();

    void setMessages(YAMLConfig config);

    GameManager getGameManager();

    void setGameManager(GameManager gameManager);


    // Other plugin managers

    SchematicManager getSchematicManager();

    void setSchematicManager(SchematicManager schematicManager);

    SWCommandManager getCommandManager();

    void setCommandManager(SWCommandManager commandManager);


    // Console

    SWCommandSender getConsoleSender();

    void setConsoleSender(SWCommandSender consoleSender);


    // Player Data

    Storage getStorage();

    void setStorage(Storage storage);

    SWPlayerDataManager getPlayerDataManager();

    void setPlayerDataManager(SWPlayerDataManager playerDataManager);

    Utilities getUtils();

    void setUtils(Utilities utils);


    // Plugin

    Logger getLogger();

    void initCommands();

    // ---- Util ----

    void disableSkyWars();

}
