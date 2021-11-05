package net.gcnt.skywarsreloaded.bukkit;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.data.config.BukkitYAMLManager;
import net.gcnt.skywarsreloaded.bukkit.data.player.BukkitSWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLManager;
import net.gcnt.skywarsreloaded.data.config.YAMLManager;
import net.gcnt.skywarsreloaded.data.player.SQLiteStorage;
import net.gcnt.skywarsreloaded.data.player.SWPlayerDataManager;
import net.gcnt.skywarsreloaded.data.player.Storage;
import net.gcnt.skywarsreloaded.data.schematic.CoreSchematicManager;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;

import java.io.File;
import java.util.logging.Logger;

public class BukkitSkyWarsReloaded extends AbstractSkyWarsReloaded {

    private BukkitSkyWarsReloadedPlugin plugin;

    public BukkitSkyWarsReloaded(BukkitSkyWarsReloadedPlugin pluginIn) {
        this.plugin = pluginIn;
    }

    @Override
    public void onEnable() {
        setYAMLManager(new BukkitYAMLManager(this));
        setConfig(getYAMLManager().loadConfig("config", getDataFolder().getAbsolutePath(), "config.yml"));


        setPlayerDataManager(new BukkitSWPlayerDataManager(this));
        setSchematicManager(new CoreSchematicManager());
        setStorage(new SQLiteStorage(this));
    }

    // Internal Utils

    @Override
    public void setPlayerDataManager(SWPlayerDataManager playerDataManager) {
        super.setPlayerDataManager(playerDataManager);
    }

    @Override
    public void setStorage(Storage storage) {
        super.setStorage(storage);
    }

    @Override
    public void setYAMLManager(YAMLManager yamlManager) {
        super.setYAMLManager(yamlManager);
    }

    public BukkitSkyWarsReloadedPlugin getPlugin() {
        return this.plugin;
    }

    // Getters

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public void setSchematicManager(SchematicManager schematicManager) {
        super.setSchematicManager(schematicManager);
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }

    @Override
    public void disableSkyWars() {

    }
}
