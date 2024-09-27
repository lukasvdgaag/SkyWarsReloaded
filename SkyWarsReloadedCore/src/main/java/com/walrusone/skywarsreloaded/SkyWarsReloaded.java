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
import com.walrusone.skywarsreloaded.managers.worlds.*;
import com.walrusone.skywarsreloaded.menus.*;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.nms.NMS;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.nms.NMSUtils;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import com.walrusone.skywarsreloaded.utilities.Util;
import com.walrusone.skywarsreloaded.utilities.holograms.HoloDisUtil;
import com.walrusone.skywarsreloaded.utilities.holograms.HologramsUtil;
import com.walrusone.skywarsreloaded.utilities.minecraftping.MinecraftPing;
import com.walrusone.skywarsreloaded.utilities.minecraftping.MinecraftPingOptions;
import com.walrusone.skywarsreloaded.utilities.minecraftping.MinecraftPingReply;
import com.walrusone.skywarsreloaded.utilities.mygcnt.GCNTUpdater;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRPlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class SkyWarsReloaded extends JavaPlugin implements PluginMessageListener {

    private static SkyWarsReloaded instance;
    private final ArrayList<String> leaderTypes = new ArrayList<>();
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
    private GameMapManager gameMapManager;

    private PlayerOptionsManager pom;

    private Config config;

    private HologramsUtil hu;
    private boolean loaded;
    private BukkitTask specObserver;

    // Utils
    private GCNTUpdater updater;
    private boolean extensionCompatible = false;
    private boolean extensionHasCompatCheck = false;

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

    public boolean isNewVersion() throws Exception {
        // Prevent mix-and-match incompatible versions of SWR & SWR-Extension
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTrace = thread.getStackTrace();

        // Check who is asking - if it's the extension, ensure use of compatibility check
        String callingClassName = stackTrace[2].getClassName();
        if (callingClassName.equals("me.gaagjescraft.network.team.skywarsreloaded.extension.SWExtension")) {
            if (extensionCompatible) return true; // everything checks out
            else if (extensionHasCompatCheck) return false; // non-legacy extension version, we can return false
            else throw new Exception("Incompatible extension version!"); // legacy extension, requires exception to prevent enable
        }
        return true;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        loaded = false;

        // NMS Init
        this.nmsHandler = NMSUtils.loadNMS(this);
        if (this.nmsHandler == null) {
            this.setEnabled(false);
            return;
        }

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

        // State using debug mode or not
        if (getCfg().debugEnabled()) this.getLogger().info("Debug mode enabled");

        // Managers
        if (this.gameMapManager == null) this.gameMapManager = new GameMapManager(this);
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
        // PER WORLD INV
        if (Bukkit.getPluginManager().isPluginEnabled("PerWorldInventory")) {
            this.getServer().getPluginManager().registerEvents(new PerWorldInventoryListener(), this);
        }
        // PAF
        if (getCfg().bungeeMode() && getCfg().isUsePartyAndFriends()) {
            // Currently disabled due to inability to access bungeecord PAF from spigot
            // this.getServer().getPluginManager().registerEvents(new PartyAndFriendsHook(), this);
        }
        // SLIME WORLD MANAGER
        if (Bukkit.getPluginManager().isPluginEnabled("SlimeWorldManager") && getCfg().isUseSlimeWorldManager()) {
            getLogger().info("SlimeWorldManager option enabled. Checking for AdvancedSlimePaper...");
            try {
                Class.forName("com.infernalsuite.aswm.SlimeNMSBridgeImpl");
                getLogger().info("Found AdvancedSlimePaper!");
                wm = (ASPWorldManager) Class.forName("com.walrusone.skywarsreloaded.managers.worlds.ASPWorldManagerImpl")
                        .getConstructor()
                        .newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().info("AdvancedSlimePaper not found");
                int serverFeatureVersion = Integer.parseInt(getServer().getVersion().split("\\.")[1]);
                if (serverFeatureVersion > 19) {
                    getLogger().info("SlimeWorldManager cannot be used on 1.20 or higher. We expected the server to be running AdvancedSlimePaper.");
                    wm = null;
                } else if (serverFeatureVersion > 14) {
                    try {
                        getLogger().info("Using ASWM World Manager");
                        wm = (WorldManager) Class.forName("com.walrusone.skywarsreloaded.managers.worlds.ASWMWorldManager")
                                .getConstructor()
                                .newInstance();
                    } catch (Exception ex) {
                        getLogger().info("Using Bukkit World Manager");
                        wm = null;
                    }
                } else {
                    try {
                        getLogger().info("Using Legacy SWM World Manager");
                        wm = (WorldManager) Class.forName("com.walrusone.skywarsreloaded.managers.worlds.LegacySWMWorldManager")
                                .getConstructor()
                                .newInstance();
                    } catch (Exception ex) {
                        getLogger().info("Using Bukkit World Manager");
                        wm = null;
                    }
                }
            }
        }

        if (wm == null) {
            getLogger().info("Using Bukkit World Manager");
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

        // Requires IM - aka load()
        new ArenasMenu();

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
                                if (data == null) return;
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
        if (gameMapManager != null) {
            for (final GameMap gameMap : SkyWarsReloaded.getGameMapMgr().getMapsCopy()) {
                if (gameMap.isEditing()) {
                    gameMap.saveMap(null);
                }
                ImmutableList<UUID> specUUIDs = ImmutableList.copyOf(gameMap.getSpectators());
                for (final UUID uuid : specUUIDs) {
                    final Player player = getServer().getPlayer(uuid);
                    if (player != null) {
                        SkyWarsReloaded.get().getPlayerManager().removePlayer(
                                player, PlayerRemoveReason.OTHER, null, false);
                    }
                }
                ImmutableList<Player> players = ImmutableList.copyOf(gameMap.getAlivePlayers());
                for (final Player player : players) {
                    if (player != null) {
                        this.getPlayerManager().removePlayer(
                                player, PlayerRemoveReason.OTHER, null, false);
                    }
                }
                getWM().deleteWorld(gameMap.getName(), false);
            }
        }
        ImmutableList<PlayerData> pDataSnapshot = ImmutableList.copyOf(PlayerData.getAllPlayerData());
        for (final PlayerData playerData : pDataSnapshot) {
            playerData.restoreToBeforeGameState(true);
        }
        PlayerData.getAllPlayerData().clear();
        for (final PlayerStat fData : PlayerStat.getPlayers()) {
            DataStorage.get().saveStats(fData);
        }
        this.gameMapManager = null;
    }

    public void load() {
        messaging = null;
        messaging = new Messaging(this);
        reloadConfig();
        config.load();
        cm = new ChestManager();
        im = new ItemsManager();
        pom = new PlayerOptionsManager();

        if (gameMapManager == null) gameMapManager = new GameMapManager(this);

        GameKit.loadkits();
        SkyWarsReloaded.getGameMapMgr().loadMaps();

        boolean sqlEnabled = getConfig().getBoolean("sqldatabase.enabled");
        if (sqlEnabled) {
            getFWDatabase();
        }
        leaderTypes.clear();

        for (LeaderType type : LeaderType.values()) {
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                leaderTypes.add(type.toString());
            }
        }

        new BukkitRunnable() {
            public void run() {
                for (final Player player : getServer().getOnlinePlayers()) {
                    if (PlayerStat.getPlayerStats(player.getUniqueId().toString()) == null) {
                        PlayerStat pStats = new PlayerStat(player);
                        PlayerStat.getPlayers().add(pStats);
                        pStats.updatePlayerIfInLobby(player);
                        pStats.loadStats(null);
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
                    for (GameMap gMap : SkyWarsReloaded.getGameMapMgr().getMapsCopy()) {
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

        try {
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
                                    if (data == null) return;
                                    final String[] serverInfo = data.getDescription().getText().split(":");
                                    swrServer.setMatchState(serverInfo[0]);
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
                        GameMap gMap = SkyWarsReloaded.getGameMapMgr().getMapsCopy().get(0);
                        String playerCount = "" + gMap.getAlivePlayers().size();
                        String maxPlayers = "" + gMap.getMaxPlayers();
                        String gameStarted = "" + gMap.getMatchState().toString();
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
        } catch (Exception ex) {
            Logger logger = instance.getLogger();
            logger.warning("Invalid plugin message was received! Enable debug mode before reporting this! (in config.yml)");
            if (config.debugEnabled()) {
                String pName = player == null ? "null" : player.getName();
                logger.info("SkywarsReloaded::onPluginMessageReceived subchannel=" + subchannel + ", playerName=" + pName);
                ex.printStackTrace();
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

    public ArrayList<String> getLeaderTypes() {
        return leaderTypes;
    }

    public PlayerStat getPlayerStat(Player player) {
        return PlayerStat.getPlayerStats(player);
    }

    public void setChestManager(ChestManager chestManager) {
        this.cm = chestManager;
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
            // Once every hour
        }, 0, 20 * 60 * 60);
    }

    public GCNTUpdater getUpdater() {
        return updater;
    }

    @SuppressWarnings("unused")
    public boolean extensionCompatCheck(JavaPlugin ext) {
        extensionHasCompatCheck = true;

        PluginDescriptionFile desc = ext.getDescription();
        String compatibleExtensionVersion = "1.7.15";
        String foundVersion = desc.getVersion();

        String[] compatVersionParts = compatibleExtensionVersion.split("\\.");
        String[] foundVersionParts = foundVersion.split("\\.");

        boolean majorMatch = compatVersionParts[0].equals(foundVersionParts[0]);
        boolean featureMatch = compatVersionParts[1].equals(foundVersionParts[1]);
        boolean patchMatch = compatVersionParts[2].equals(foundVersionParts[2]);

        if (!patchMatch) {
            try {
                int compatPatchVer = Integer.parseInt(compatVersionParts[2]);
                int foundPatchVer = Integer.parseInt(foundVersionParts[2]);
                if (foundPatchVer > compatPatchVer) {
                    this.getLogger().warning(String.format(
                            "You are using a newer Skywars-Extension version than expected but this should still work (%s). " +
                                    "This message is for debugging purposes. Skywars will attempt to start as normal.",
                            foundVersion
                    ));

                    // Allow newer patch versions
                    patchMatch = true;
                }
            } catch (Exception ignored) {
            }
        }

        if (desc.getName().equals("Skywars-Extension") && majorMatch && featureMatch && patchMatch) {
            extensionCompatible = true;
            return true;
        } else {
            String msg = "\n" +
                    "-------------------------------------------------------------------------------\n" +
                    "You are trying to load an incompatible version of the Skywars-Extension plugin!\n" +
                    "Expected version %s but found %s! Make sure you download the latest version on\n" +
                    "SpigotMC or our website. You won't receive support for using outdated versions!\n" +
                    "-------------------------------------------------------------------------------\n";
            this.getLogger().severe(String.format(msg, compatibleExtensionVersion, foundVersion));
            return false;
        }
    }

    public GameMapManager getGameMapManager() {
        return this.gameMapManager;
    }

    public void setGameMapManager(GameMapManager gameMapManager) {
        this.gameMapManager = gameMapManager;
    }

    public static GameMapManager getGameMapMgr() {
        return instance.gameMapManager;
    }
}
