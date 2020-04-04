package com.walrusone.skywarsreloaded;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.walrusone.skywarsreloaded.api.NMS;
import com.walrusone.skywarsreloaded.commands.CmdManager;
import com.walrusone.skywarsreloaded.commands.KitCmdManager;
import com.walrusone.skywarsreloaded.commands.MapCmdManager;
import com.walrusone.skywarsreloaded.commands.PartyCmdManager;
import com.walrusone.skywarsreloaded.config.Config;
import com.walrusone.skywarsreloaded.database.DataStorage;
import com.walrusone.skywarsreloaded.database.Database;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerData;
import com.walrusone.skywarsreloaded.listeners.*;
import com.walrusone.skywarsreloaded.managers.*;
import com.walrusone.skywarsreloaded.menus.*;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.holograms.HoloDisUtil;
import com.walrusone.skywarsreloaded.utilities.holograms.HologramsUtil;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRMVdWPlaceholder;
import com.walrusone.skywarsreloaded.utilities.placeholders.SWRPlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SkyWarsReloaded extends JavaPlugin implements PluginMessageListener {

    private static SkyWarsReloaded instance;
    private static Database db;
    private ArrayList<String> useable = new ArrayList<>();
    private Messaging messaging;
    private Leaderboard leaderboard;
    private IconMenuController ic;
    private ItemsManager im;
    private PlayerOptionsManager pom;
    private Config config;
    private ChestManager cm;
    private WorldManager wm;
    private String servername;
    private NMS nmsHandler;
    private HologramsUtil hu;
    private boolean loaded;
    private BukkitTask specObserver;

    public static SkyWarsReloaded get() {
        return instance;
    }

    public static Messaging getMessaging() {
        return instance.messaging;
    }

    public static Leaderboard getLB() {
        return instance.leaderboard;
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
        return db;
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

        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            final Class<?> clazz = Class.forName("com.walrusone.skywarsreloaded.nms." + version + ".NMSHandler");
            // Check if we have a NMSHandler class at that location.
            if (NMS.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                this.nmsHandler = (NMS) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Could not find support for this CraftBukkit version: " + version + ".");
            this.getLogger().info("Check for updates at https://www.spigotmc.org/resources/skywarsreloaded.3796/");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Loading support for " + version);

        servername = "none";

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

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
        reloadConfig();

        config = new Config();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SWRPlaceholderAPI().register();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            new SWRMVdWPlaceholder(this);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PerWorldInventory")) {
            this.getServer().getPluginManager().registerEvents(new PerWorldInventoryListener(), this);
        }

        ic = new IconMenuController();
        wm = new WorldManager();

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

        load();
        if (SkyWarsReloaded.getCfg().tauntsEnabled()) {
            this.getServer().getPluginManager().registerEvents(new TauntListener(), this);
        }
        if (SkyWarsReloaded.getCfg().particlesEnabled()) {
            this.getServer().getPluginManager().registerEvents(new ParticleEffectListener(), this);
        }
        if (config.disableCommands()) {
            this.getServer().getPluginManager().registerEvents(new PlayerCommandPrepocessListener(), this);
        }

        if (getCfg().bungeeMode()) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
            Bukkit.getPluginManager().registerEvents(new PingListener(), this);
        }
        if (getCfg().bungeeMode()) {
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
        }
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
                    MatchManager.get().playerLeave(player, DamageCause.CUSTOM, true, false, true);
                }
            }
            getWM().deleteWorld(gameMap.getName());
        }
        for (final PlayerData playerData : PlayerData.getPlayerData()) {
            playerData.restore(false);
        }
        PlayerData.getPlayerData().clear();
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
        useable.clear();

        for (LeaderType type : LeaderType.values()) {
            if (SkyWarsReloaded.getCfg().isTypeEnabled(type)) {
                useable.add(type.toString());
            }
        }

        new BukkitRunnable() {
            public void run() {
                for (final Player v : getServer().getOnlinePlayers()) {
                    if (PlayerStat.getPlayerStats(v.getUniqueId().toString()) == null) {
                        PlayerStat.getPlayers().add(new PlayerStat(v));
                    }
                }
                leaderboard = null;
                leaderboard = new Leaderboard();
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

        getCommand("skywars").setExecutor(new CmdManager());
        getCommand("swkit").setExecutor(new KitCmdManager());
        getCommand("swmap").setExecutor(new MapCmdManager());
        if (config.partyEnabled()) {
            getCommand("swparty").setExecutor(new PartyCmdManager());
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

        if (subchannel.equals("SWRMessaging")) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String header = msgin.readUTF();
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

    public String getServerName() {
        return servername;
    }

    public ArrayList<String> getUseable() {
        return useable;
    }

    public PlayerStat getPlayerStat(Player player) {
        return PlayerStat.getPlayerStats(player);
    }

    public boolean serverLoaded() {
        return loaded;
    }

}