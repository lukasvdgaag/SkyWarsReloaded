package net.gcnt.skywarsreloaded;

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

    void setYAMLManager(YAMLManager yamlManager);

    SchematicManager getSchematicManager();

    void setSchematicManager(SchematicManager schematicManager);

    YAMLConfig getConfig();

    void setConfig(YAMLConfig config);

    // Player Data
    Storage getStorage();

    void setStorage(Storage storage);

    SWPlayerDataManager getPlayerDataManager();

    void setPlayerDataManager(SWPlayerDataManager playerDataManager);


    // Plugin

    Logger getLogger();

    // ---- Setters ----

    // ---- Util ----

    void disableSkyWars();

}
