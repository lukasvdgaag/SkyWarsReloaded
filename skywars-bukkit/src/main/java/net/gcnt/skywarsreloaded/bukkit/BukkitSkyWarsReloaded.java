package net.gcnt.skywarsreloaded.bukkit;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.command.BukkitSWCommandExecutor;
import net.gcnt.skywarsreloaded.bukkit.data.config.BukkitYAMLManager;
import net.gcnt.skywarsreloaded.bukkit.data.player.BukkitSWPlayerDataManager;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitUtils;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWConsoleSender;
import net.gcnt.skywarsreloaded.command.CoreSWCommandManager;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;

import java.io.File;
import java.util.logging.Logger;

public class BukkitSkyWarsReloaded extends AbstractSkyWarsReloaded {

    private final BukkitSkyWarsReloadedPlugin plugin;

    public BukkitSkyWarsReloaded(BukkitSkyWarsReloadedPlugin pluginIn) {
        this.plugin = pluginIn;
    }

    // Internal Utils

    @Override
    public void initUtilities() {
        setUtils(new BukkitUtils());
    }

    @Override
    public void initYAMLManager() {
        setYAMLManager(new BukkitYAMLManager(this));
    }

    @Override
    public void initPlayerDataManager() {
        setPlayerDataManager(new BukkitSWPlayerDataManager(this));
    }

    @Override
    public void initCommandManager() {
        setCommandManager(new CoreSWCommandManager(this));
    }

    @Override
    public void initCommands() {
        BukkitSWCommandExecutor ex = new BukkitSWCommandExecutor(this);
        plugin.getCommand("skywars").setExecutor(ex);
    }

    @Override
    public void initConsoleSender() {
        setConsoleSender(new BukkitSWConsoleSender(this.getPlugin().getServer().getConsoleSender()));
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
