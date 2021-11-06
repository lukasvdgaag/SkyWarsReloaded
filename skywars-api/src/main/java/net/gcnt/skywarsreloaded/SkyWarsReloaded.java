package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;

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

    YAMLConfig getConfig();

    void setYAMLManager(YAMLManager yamlManager);

    void setConfig(YAMLConfig config);

    // Other plugin managers

    SchematicManager getSchematicManager();

    SWCommandManager getCommandManager();

    void setSchematicManager(SchematicManager schematicManager);

    void setCommandManager(SWCommandManager commandManager);

    // Player Data

    Storage getStorage();

    SWPlayerDataManager getPlayerDataManager();

    void setStorage(Storage storage);

    void setPlayerDataManager(SWPlayerDataManager playerDataManager);

    // Plugin

    Logger getLogger();

    // ---- Util ----

    void disableSkyWars();

}
