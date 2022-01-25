package net.gcnt.skywarsreloaded.bukkit;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.command.BukkitSWCommandExecutor;
import net.gcnt.skywarsreloaded.bukkit.data.config.BukkitYAMLManager;
import net.gcnt.skywarsreloaded.bukkit.data.player.BukkitSWPlayerDataManager;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitGameManager;
import net.gcnt.skywarsreloaded.bukkit.game.chest.BukkitChestManager;
import net.gcnt.skywarsreloaded.bukkit.game.kits.BukkitKitManager;
import net.gcnt.skywarsreloaded.bukkit.game.loader.SchemWorldLoader;
import net.gcnt.skywarsreloaded.bukkit.game.loader.SlimeWorldLoader;
import net.gcnt.skywarsreloaded.bukkit.listener.BukkitSWEventListener;
import net.gcnt.skywarsreloaded.bukkit.managers.BukkitPlayerManager;
import net.gcnt.skywarsreloaded.bukkit.protocol.BukkitNMSManager;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitPlatformUtils;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitSWLogger;
import net.gcnt.skywarsreloaded.bukkit.wrapper.scheduler.BukkitSWScheduler;
import net.gcnt.skywarsreloaded.bukkit.wrapper.sender.BukkitSWConsoleSender;
import net.gcnt.skywarsreloaded.bukkit.wrapper.server.BukkitSWServer;
import net.gcnt.skywarsreloaded.command.CoreSWCommandManager;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class BukkitSkyWarsReloaded extends AbstractSkyWarsReloaded {

    private final BukkitSkyWarsReloadedPlugin plugin;

    public BukkitSkyWarsReloaded(BukkitSkyWarsReloadedPlugin pluginIn) {
        this.plugin = pluginIn;
    }

    // Internal Utils

    @Override
    public void initChestManager() {
        setChestManager(new BukkitChestManager(this));
    }

    @Override
    public void initCommandManager() {
        setCommandManager(new CoreSWCommandManager(this));
    }

    @Override
    public void initCommands() {
        BukkitSWCommandExecutor ex = new BukkitSWCommandExecutor(this);

        PluginCommand plCmdSW = plugin.getCommand("skywars");
        if (plCmdSW != null) {
            plCmdSW.setExecutor(ex);
            plCmdSW.setTabCompleter(ex);
        }
        PluginCommand plCmdSWMap = plugin.getCommand("skywarsmap");
        if (plCmdSWMap != null) {
            plCmdSWMap.setExecutor(ex);
            plCmdSWMap.setTabCompleter(ex);
        }
        PluginCommand plCmdSWKit = plugin.getCommand("skywarskit");
        if (plCmdSWKit != null) {
            plCmdSWKit.setExecutor(ex);
            plCmdSWKit.setTabCompleter(ex);
        }
    }

    @Override
    public void initConsoleSender() {
        setConsoleSender(new BukkitSWConsoleSender(this.getBukkitPlugin().getServer().getConsoleSender()));
    }

    @Override
    public void initEventListener() {
        BukkitSWEventListener bukkitEventListener = new BukkitSWEventListener(this);
        this.plugin.getServer().getPluginManager().registerEvents(bukkitEventListener, this.plugin);
        setEventListener(bukkitEventListener);
    }

    @Override
    public void initGameManager() {
        setGameManager(new BukkitGameManager(this));
    }

    @Override
    public void initKitManager() {
        setKitManager(new BukkitKitManager(this));
    }

    @Override
    public void initLogger() {
        setLogger(new BukkitSWLogger(this, this.plugin.getLogger(), false));
    }

    @Override
    public void initNMSManager() throws IllegalStateException {
        setNMSManager(new BukkitNMSManager(this));
    }

    @Override
    public void initPlatformUtils() {
        setPlatformUtils(new BukkitPlatformUtils(this));
    }

    @Override
    public void initPlayerDataManager() {
        setPlayerDataManager(new BukkitSWPlayerDataManager(this));
    }

    @Override
    public void initPlayerManager() {
        setPlayerManager(new BukkitPlayerManager(this));
    }

    @Override
    protected void initScheduler() {
        setScheduler(new BukkitSWScheduler(this));
    }

    @Override
    public void initServer() {
        setServer(new BukkitSWServer(this));
    }

    @Override
    public void initWorldLoader() {
        if (getConfig().getBoolean(ConfigProperties.ENABLE_SLIME_WORLD_MANAGER.toString(), false)) {
            if (((Plugin) plugin).getServer().getPluginManager().isPluginEnabled("SlimeWorldManager")) {
                // SWM found! yay
                setWorldLoader(new SlimeWorldLoader(this));
                return;
            }
        }

        // Fallback to schematic loading
        setWorldLoader(new SchemWorldLoader(this));
    }

    @Override
    public void initYAMLManager() {
        setYAMLManager(new BukkitYAMLManager(this));
    }

    public BukkitSkyWarsReloadedPlugin getBukkitPlugin() {
        return this.plugin;
    }

    // Getters

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public String getMinecraftVersion() {
        return this.plugin.getServer().getVersion();
    }

    @Override
    public String getPlatformVersion() {
        // todo not sure if this is correct, TBD
        return this.plugin.getServer().getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void disableSkyWars() {

    }
}
