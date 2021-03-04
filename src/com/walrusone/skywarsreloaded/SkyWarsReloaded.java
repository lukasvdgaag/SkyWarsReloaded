package com.walrusone.skywarsreloaded;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.walrusone.skywarsreloaded.api.SkywarsReloadedAPI;
import com.walrusone.skywarsreloaded.api.impl.SkywarsReloadedImpl;
import com.walrusone.skywarsreloaded.commands.*;
import com.walrusone.skywarsreloaded.config.Config;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.database.Database;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerData;
import com.walrusone.skywarsreloaded.listeners.*;
import com.walrusone.skywarsreloaded.managers.*;
import com.walrusone.skywarsreloaded.managers.worlds.FileWorldManager;
import com.walrusone.skywarsreloaded.managers.worlds.SWMWorldManager;
import com.walrusone.skywarsreloaded.managers.worlds.WorldManager;
import com.walrusone.skywarsreloaded.menus.*;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.nms.NMS;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.PartyAndFriendsHook;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.holograms.HoloDisUtil;
import com.walrusone.skywarsreloaded.utilities.holograms.HologramsUtil;
import com.walrusone.skywarsreloaded.utilities.minecraftping.MinecraftPing;
import com.walrusone.skywarsreloaded.utilities.minecraftping.MinecraftPingOptions;
import com.walrusone.skywarsreloaded.utilities.minecraftping.MinecraftPingReply;
import com.walrusone.skywarsreloaded.utilities.mygcnt.GCNTUpdater;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRMVdWPlaceholder;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRPlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SkyWarsReloaded extends JavaPlugin implements PluginMessageListener {

    private static SkyWarsReloaded instance;
    private final ArrayList<String> usable = new ArrayList<>();
    private final Object leaderboardLock = new Object();
    private String servername;
    private Database db;
    private NMS nmsHandler;
    private SkywarsReloadedAPI swrAPI = null;
    // Managers
    private MatchManager matchManager = null;
    private PlayerManager playerManager = null;
    private MainCmdManager mainCmdManager = null;
    private KitCmdManager kitCmdManager = null;
    private MapCmdManager mapCmdManager = null;
    private PartyCmdManager partyCmdManager = null;
    private SWTabCompleter swTabCompleter = null;
    private ChestManager cm = null;
    private WorldManager wm = null;
    private Messaging messaging;
    private Leaderboard leaderboard = null;
    private IconMenuController ic;
    private ItemsManager im;

    private PlayerOptionsManager pom;

    private Config config;

    private HologramsUtil hu;
    private boolean loaded;
    private BukkitTask specObserver;

    private GCNTUpdater updater;

    public static SkyWarsReloaded get() {
        return instance;
    }

    public static SkywarsReloadedAPI getAPI() {
        return get().swrAPI;
    }

    public static Messaging getMessaging() {
        return instance.messaging;
    }

    public static Leaderboard getLB() {
        // TODO: Make non static
        synchronized (instance.leaderboardLock) {
            return instance.leaderboard;
        }
    }

    public static Config getCfg() {
        return instance.config;
    }

    public static IconMenuController getIC() {
        return instance.ic;
    }

    public static WorldManager getWM() {
        return instance.wm;
    }

    public static Database getDb() {
        return instance.db;
    }

    public static ChestManager getCM() {
        return instance.cm;
    }

    public static ItemsManager getIM() {
        return instance.im;
    }

    public static NMS getNMS() {
        return instance.nmsHandler;
    }

    public static HologramsUtil getHoloManager() {
        return instance.hu;
    }

    public static PlayerOptionsManager getOM() {
        return instance.pom;
    }

    public boolean isNewVersion() {
        return true;
    }

    public void onEnable() {
        loaded = false;
        instance = this;

        // NMS Init
        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            final Class<?> clazz = Class.forName("com.walrusone.skywarsreloaded.nms." + version + ".NMSHandler");
            // Check if we have a NMSHandler class at that location.
            if (NMS.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                this.nmsHandler = (NMS) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException
                | IllegalArgumentException e) {
            this.getLogger().severe("Could not find support for this CraftBukkit version: " + version + ". Now disabling the plugin!");
            this.getLogger().info("Check for updates at https://gaagjescraft.net/download/skywars");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Loading support for " + version);

        // Updater init
        this.updater = new GCNTUpdater();

        servername = "none";

        // Load config for 1.8
        if (nmsHandler.getVersion() < 9) {
            File config = new File(SkyWarsReloaded.get().getDataFolder(), "config.yml");
            if (!config.exists()) {
                SkyWarsReloaded.get().saveResource("config18.yml", false);
                config = new File(SkyWarsReloaded.get().getDataFolder(), "config18.yml");
                if (config.exists()) {
                    boolean result = config.renameTo(new File(SkyWarsReloaded.get().getDataFolder(), "config.yml"));
                    if (result) {
                        getLogger().info("Loading 1.8 Configuration Files");
                    }
                }
            }
            // Load config for 1.12
        } else if (nmsHandler.getVersion() < 13 && nmsHandler.getVersion() > 8) {
            File config = new File(SkyWarsReloaded.get().getDataFolder(), "config.yml");
            if (!config.exists()) {
                SkyWarsReloaded.get().saveResource("config112.yml", false);
                config = new File(SkyWarsReloaded.get().getDataFolder(), "config112.yml");
                if (config.exists()) {
                    boolean result = config.renameTo(new File(SkyWarsReloaded.get().getDataFolder(), "config.yml"));
                    if (result) {
                        getLogger().info("Loading 1.9 - 1.12 Configuration Files");
                    }
                }
            }
        }
        // Copy missing attributes
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
        reloadConfig();

        // Load config data
        config = new Config();

        // Managers
        matchManager = MatchManager.get();
        playerManager = new PlayerManager(this);

        // Cages
        File cagesFolder = new File(getDataFolder(), "cages");
        if (!cagesFolder.exists()) {
            if (!cagesFolder.mkdir()) {
                getLogger().severe("Failed to create the cages folder.");
            }
        }

        // ------ All external integrations --------
        // PAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SWRPlaceholderAPI().register();
        }
        // MVdWPAPI
        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            new SWRMVdWPlaceholder(this);
        }
        // PER WORLD INV
        if (Bukkit.getPluginManager().isPluginEnabled("PerWorldInventory")) {
            this.getServer().getPluginManager().registerEvents(new PerWorldInventoryListener(), this);
        }
        // PAF
        if (getCfg().bungeeMode() && getCfg().isUsePartyAndFriends()) {
            this.getServer().getPluginManager().registerEvents(new PartyAndFriendsHook(), this);
        }
        // SLIME WORLD MANAGER
        if (Bukkit.getPluginManager().isPluginEnabled("SlimeWorldManager") && getCfg().isUseSlimeWorldManager()) {
            wm = new SWMWorldManager();
        } else {
            wm = new FileWorldManager();
        }

        // MENUS
        ic = new IconMenuController();

        // LISTENERS
        if (nmsHandler.getVersion() > 8) {
            this.getServer().getPluginManager().registerEvents(new SwapHandListener(), this);
        }
        this.getServer().getPluginManager().registerEvents(ic, this);
        this.getServer().getPluginManager().registerEvents(new ArenaDamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        this.getServer().getPluginManager().registerEvents(new LobbyListener(), this);
        this.getServer().getPluginManager().registerEvents(new SpectateListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getServer().getPluginManager().registerEvents(new ProjectileSpleefListener(), this);

        // LOAD BEFORE HOLO - Holo needs server to be loaded to update correctly
        load();

        // Holograms
        if (SkyWarsReloaded.getCfg().hologramsEnabled()) {
            hu = null;
            if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                hu = new HoloDisUtil();
                hu.load();
            }
            if (hu == null) {
                config.setHologramsEnabled(false);
                config.save();
            }
        }

        // Taunts
        if (SkyWarsReloaded.getCfg().tauntsEnabled()) {
            this.getServer().getPluginManager().registerEvents(new TauntListener(), this);
        }
        // Particles
        if (SkyWarsReloaded.getCfg().particlesEnabled()) {
            this.getServer().getPluginManager().registerEvents(new ParticleEffectListener(), this);
        }
        // Disabled commands
        if (config.disableCommands()) {
            this.getServer().getPluginManager().registerEvents(new PlayerCommandPrepocessListener(), this);
        }

        // Plugin messaging channels
        if (getCfg().bungeeMode()) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
            Bukkit.getPluginManager().registerEvents(new PingListener(), this);
        }
        if (getCfg().bungeeMode() && getCfg().isLobbyServer()) {
            new BukkitRunnable() {
                public void run() {
                    if (servername.equalsIgnoreCase("none")) {
                        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                        if (player != null) {
                            sendBungeeMsg(player, "GetServer", "none");
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this, 0, 20);

            prepareServers();

            SWRServer.updateServerSigns();

        }
        checkUpdates();
        // TODO: SWR API - Not finished
        swrAPI = new SkywarsReloadedImpl();
    }

    public void prepareServers() {
        SWRServer.clearServers();
        for (String server : getCfg().getGameServers()) {
            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                SkyWarsReloaded.get().getLogger().warning(
                        "Setting up pinging service for " + server);
            }
            final String[] serverParts = server.split(":");
            if (serverParts.length >= 5) {
                SWRServer.addServer(new SWRServer(serverParts[0], Integer.parseInt(serverParts[1])));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SWRServer swrServer = SWRServer.getServer(serverParts[0]);
                        if (swrServer != null) {
                            swrServer.setDisplayName(serverParts[2]);
                            swrServer.setMaxPlayers(Integer.parseInt(serverParts[3]));
                            swrServer.setTeamsize(Integer.parseInt(serverParts[4]));
                            if (serverParts.length == 6) {
                                swrServer.setHostname(serverParts[5]);
                            }

                            /*Player player = Iterables.getFirst(SkyWarsReloaded.get().getServer().getOnlinePlayers(), null);
                            if (player != null) {
                                Bukkit.getLogger().warning("Data we're trying to send: " + serverParts[0]);
                                sendBungeeMsg(player, "PlayerCount", serverParts[0]);
                            } else {*/
                            try {
                                String hostname = swrServer.getHostname() == null ? "127.0.0.1" : swrServer.getHostname();

                                MinecraftPingReply data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(hostname).setPort(swrServer.getPort()));
                                final String[] serverInfo = data.getDescription().getText().split(":");
                                if (serverInfo.length < 3) {
                                    SkyWarsReloaded.get().getLogger().warning("Skywars Server Ping failed! " +
                                            hostname + ":" + swrServer.getPort() + " failed to return valid ping!\n" +
                                            "(" + data.getDescription().getText() + ")");
                                    return;
                                }
                                swrServer.setMatchState(serverInfo[0]);
                                swrServer.setPlayerCount(Integer.parseInt(serverInfo[1]));
                                swrServer.setMaxPlayers(Integer.parseInt(serverInfo[2]));
                            } catch (IOException e) {
                                swrServer.setMatchState(MatchState.OFFLINE);
                                e.printStackTrace();
                            } finally {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        swrServer.updateSigns();
                                    }
                                }.runTask(SkyWarsReloaded.get());
                            }
                            /*}*/
                        }
                    }
                }.runTaskTimerAsynchronously(this, 20, 200);
            }
        }
    }

    public void updateServers() {
        new BukkitRunnable() {
            public void run() {
                for (SWRServer server : SWRServer.getServersCopy()) {
                    Player player = Iterables.getFirst(SkyWarsReloaded.get().getServer().getOnlinePlayers(), null);
                    if (player != null) {
                        sendBungeeMsg(player, "PlayerCount", server.getServerName());
                    }
                }
            }
        }.runTask(this);
    }

    public void onDisable() {
        loaded = false;
        this.getServer().getScheduler().cancelTasks(this);
        for (final GameMap gameMap : GameMap.getMaps()) {
            if (gameMap.isEditing()) {
                gameMap.saveMap(null);
            }
            for (final UUID uuid : gameMap.getSpectators()) {
                final Player player = getServer().getPlayer(uuid);
                if (player != null) {
                    MatchManager.get().removeSpectator(player);
                }
            }
            for (final Player player : gameMap.getAlivePlayers()) {
                if (player != null) {
                    this.getPlayerManager().removePlayer(player, PlayerRemoveReason.OTHER, null, false);
                    //MatchManager.get().removeAlivePlayer(player, DamageCause.CUSTOM, true, false);
                }
            }
            getWM().deleteWorld(gameMap.getName(), false);
        }
        ImmutableList<PlayerData> pDataSnapshot = ImmutableList.copyOf(PlayerData.getAllPlayerData());
        for (final PlayerData playerData : pDataSnapshot) {
            playerData.restoreToBeforeGameState(true);
        }
        PlayerData.getAllPlayerData().clear();
        for (final PlayerStat fData : PlayerStat.getPlayers()) {
            DataStorage.get().saveStats(fData);
        }
    }

    public void load() {
        messaging = null;
        messaging = new Messaging(this);
        reloadConfig();
        config.load();
        cm = new ChestManager();
        im = new ItemsManager();
        pom = new PlayerOptionsManager();
        GameKit.loadkits();
        GameMap.loadMaps();

        boolean sqlEnabled = getConfig().getBoolean("sqldatabase.enabled");
        if (sqlEnabled) {
            getFWDatabase();
        }
        usable.clear();

        for (LeaderType type : LeaderType.values()) {
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                usable.add(type.toString());
            }
        }

        new BukkitRunnable() {
            public void run() {
                for (final Player v : getServer().getOnlinePlayers()) {
                    if (PlayerStat.getPlayerStats(v.getUniqueId().toString()) == null) {
                        PlayerStat pStats = new PlayerStat(v);
                        pStats.loadStats(null);
                        PlayerStat.getPlayers().add(pStats);
                    }
                }
                synchronized (leaderboardLock) {
                    leaderboard = new Leaderboard();
                }
            }
        }.runTaskAsynchronously(this);

        if (SkyWarsReloaded.getCfg().economyEnabled()) {
            if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
                SkyWarsReloaded.getCfg().setEconomyEnabled(false);
            }
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                SkyWarsReloaded.getCfg().setEconomyEnabled(false);
            }
        }

        if (SkyWarsReloaded.getCfg().joinMenuEnabled() || SkyWarsReloaded.getCfg().spectateMenuEnabled()) {
            new JoinMenu();
            new JoinSingleMenu();
            new JoinTeamMenu();
        }
        if (SkyWarsReloaded.getCfg().spectateMenuEnabled()) {
            new SpectateMenu();
            new SpectateSingleMenu();
            new SpectateTeamMenu();
        }

        swTabCompleter = new SWTabCompleter();

        mainCmdManager = new MainCmdManager();
        getCommand("skywars").setExecutor(mainCmdManager);
        getCommand("skywars").setTabCompleter(swTabCompleter);

        kitCmdManager = new KitCmdManager();
        getCommand("swkit").setExecutor(kitCmdManager);
        getCommand("swkit").setTabCompleter(swTabCompleter);

        mapCmdManager = new MapCmdManager();
        getCommand("swmap").setExecutor(mapCmdManager);
        getCommand("swmap").setTabCompleter(swTabCompleter);

        if (config.partyEnabled()) {
            partyCmdManager = new PartyCmdManager();
            getCommand("swparty").setExecutor(partyCmdManager);
        }
        if (getCfg().borderEnabled()) {
            if (specObserver != null) {
                specObserver.cancel();
            }
            specObserver = new BukkitRunnable() {
                @Override
                public void run() {
                    for (GameMap gMap : GameMap.getMaps()) {
                        gMap.checkSpectators();
                    }
                }
            }.runTaskTimer(SkyWarsReloaded.get(), 0, 40);
        }
        loaded = true;
    }

    private void getFWDatabase() {
        try {
            db = new Database();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        try {
            db.createTables();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("GetServer")) {
            servername = in.readUTF();
        }

        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playercount = in.readInt();

            final SWRServer swrServer = SWRServer.getServer(server);

            if (swrServer != null) {
                if (playercount == 0) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                String hostname = swrServer.getHostname() == null ? "127.0.0.1" : swrServer.getHostname();

                                MinecraftPingReply data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(hostname).setPort(swrServer.getPort()));
                                final String[] serverInfo = data.getDescription().getText().split(":");
                                swrServer.setMatchState(serverInfo[0]);
                                /*if (Util.get().isInteger(serverInfo[1])) {
                                    swrServer.setPlayerCount(Integer.parseInt(serverInfo[1]));
                                }
                                if (Util.get().isInteger(serverInfo[2])) {
                                    swrServer.setMaxPlayers(Integer.parseInt(serverInfo[2]));
                                }
                                swrServer.setDisplayName(serverInfo[3]);*/
                                swrServer.updateSigns();

                            } catch (IOException e) {
                                swrServer.setMatchState(MatchState.OFFLINE);
                                swrServer.updateSigns();
                            }
                        }
                    }.runTask(this);
                } else {
                    if (player != null) {
                        ArrayList<String> messages = new ArrayList<String>();
                        messages.add("RequestUpdate");
                        messages.add(servername);
                        sendSWRMessage(player, server, messages);
                    }
                }
            }
        }

        if (subchannel.equals("SWRMessaging")) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String header = msgin.readUTF();
                if (header.equalsIgnoreCase("ServerUpdate")) {
                    String server = msgin.readUTF();
                    String playerCount = msgin.readUTF();
                    String maxPlayers = msgin.readUTF();
                    String gameStarted = msgin.readUTF();
                    SWRServer swrServer = SWRServer.getServer(server);
                    if (swrServer != null) {
                        if (Util.get().isInteger(playerCount)) {
                            swrServer.setPlayerCount(Integer.parseInt(playerCount));
                        }
                        if (Util.get().isInteger(maxPlayers)) {
                            swrServer.setMaxPlayers(Integer.parseInt(maxPlayers));
                        }
                        swrServer.setMatchState(gameStarted);
                        swrServer.updateSigns();
                    }
                }
                if (header.equalsIgnoreCase("RequestUpdate")) {
                    String sendToServer = msgin.readUTF();
                    String playerCount = "" + GameMap.getMaps().get(0).getAlivePlayers().size();
                    String maxPlayers = "" + GameMap.getMaps().get(0).getMaxPlayers();
                    String gameStarted = "" + GameMap.getMaps().get(0).getMatchState().toString();
                    ArrayList<String> messages = new ArrayList<>();
                    messages.add("ServerUpdate");
                    messages.add(servername);
                    messages.add(playerCount);
                    messages.add(maxPlayers);
                    messages.add(gameStarted);
                    sendSWRMessage(player, sendToServer, messages);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendBungeeMsg(Player player, String subchannel, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        if (!message.equalsIgnoreCase("none")) {
            out.writeUTF(message);
        }
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public void sendSWRMessage(Player player, String server, ArrayList<String> messages) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF(server);
        out.writeUTF("SWRMessaging");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            for (String msg : messages) {
                msgout.writeUTF(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }


    // GETTERS AND SETTERS

    public MatchManager getMatchManager() {
        return this.matchManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public MainCmdManager getMainCmdManager() {
        return this.mainCmdManager;
    }

    public KitCmdManager getKitCmdManager() {
        return this.kitCmdManager;
    }

    public MapCmdManager getMapCmdManager() {
        return this.mapCmdManager;
    }

    public PartyCmdManager getPartyCmdManager() {
        return this.partyCmdManager;
    }

    public SWTabCompleter getSwTabCompleter() {
        return this.swTabCompleter;
    }

    public String getServerName() {
        return servername;
    }

    public ArrayList<String> getUsable() {
        return usable;
    }

    public PlayerStat getPlayerStat(Player player) {
        return PlayerStat.getPlayerStats(player);
    }

    public boolean serverLoaded() {
        return loaded;
    }

    public void checkUpdates() {
        this.updater = new GCNTUpdater();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            updater.checkForUpdate();
            if (updater.getUpdateStatus() == 1) {
                Bukkit.getLogger().info("====================");
                Bukkit.getLogger().info("SkyWarsReloaded Updater");
                Bukkit.getLogger().info("");
                Bukkit.getLogger().info("We found a newer version of SkyWarsReloaded!");
                Bukkit.getLogger().info("");
                Bukkit.getLogger().info("New version: " + updater.getLatestVersion());
                Bukkit.getLogger().info("Your version: " + updater.getCurrentVersion());
                Bukkit.getLogger().info("");
                Bukkit.getLogger().info("You can download it here:");
                Bukkit.getLogger().info(updater.getUpdateURL());
                Bukkit.getLogger().info("----------------------------------");
            }

        }, 0, 20 * 60 * 60);
    }

    public GCNTUpdater getUpdater() {
        return updater;
    }
}