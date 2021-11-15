package net.gcnt.skywarsreloaded.bukkit;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.command.BukkitSWCommandExecutor;
import net.gcnt.skywarsreloaded.bukkit.data.config.BukkitYAMLManager;
import net.gcnt.skywarsreloaded.bukkit.data.player.BukkitSWPlayerDataManager;
import net.gcnt.skywarsreloaded.bukkit.game.chest.BukkitChestManager;
import net.gcnt.skywarsreloaded.bukkit.game.kits.BukkitKitManager;
import net.gcnt.skywarsreloaded.bukkit.listener.BukkitSWEventListener;
import net.gcnt.skywarsreloaded.bukkit.managers.BukkitPlayerManager;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitPlatformUtils;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitSWLogger;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWConsoleSender;
import net.gcnt.skywarsreloaded.command.CoreSWCommandManager;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;

import java.io.File;

public class BukkitSkyWarsReloaded extends AbstractSkyWarsReloaded {

    private final BukkitSkyWarsReloadedPlugin plugin;

    public BukkitSkyWarsReloaded(BukkitSkyWarsReloadedPlugin pluginIn) {
        this.plugin = pluginIn;
    }

    // Internal Utils

    @Override
    public void initLogger() {
        setLogger(new BukkitSWLogger(this.plugin.getLogger(), false));
    }

    @Override
    public void initPlatformUtils() {
        setPlatformUtils(new BukkitPlatformUtils());
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
        plugin.getCommand("skywars").setTabCompleter(ex);
        plugin.getCommand("skywarsmap").setExecutor(ex);
        plugin.getCommand("skywarsmap").setTabCompleter(ex);
        plugin.getCommand("skywarskit").setExecutor(ex);
        plugin.getCommand("skywarskit").setTabCompleter(ex);
    }

    @Override
    public void initConsoleSender() {
        setConsoleSender(new BukkitSWConsoleSender(this.getPlugin().getServer().getConsoleSender()));
    }

    @Override
    public void initKitManager() {
        setKitManager(new BukkitKitManager(this));
    }

    @Override
    public void initChestManager() {
        setChestManager(new BukkitChestManager(this));
    }

    @Override
    public void initEventListener() {
        BukkitSWEventListener bukkitEventListener = new BukkitSWEventListener(this);
        this.plugin.getServer().getPluginManager().registerEvents(bukkitEventListener, this.plugin);
        setEventListener(bukkitEventListener);
    }

    @Override
    public void initPlayerManager() {
        setPlayerManager(new BukkitPlayerManager(this));
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
    public void disableSkyWars() {

    }
}
