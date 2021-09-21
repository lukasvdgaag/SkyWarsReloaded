package net.gcnt.skywarsreloaded;

import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;

import java.io.File;
import java.util.logging.Logger;

public interface SkyWarsReloaded {

    // ---- Getters ----

    // Configs

    File getDataFolder();

    YAMLManager getYAMLManager();

    // Player Data

    Storage getStorage();

    SWPlayerDataManager getPlayerDataManager();

    // Plugin

    Logger getLogger();

    // ---- Setters ----

    // ---- Util ----

    void disableSkyWars();

}
