package com.walrusone.skywarsreloaded.game;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.*;
import com.walrusone.skywarsreloaded.events.SkyWarsJoinEvent;
import com.walrusone.skywarsreloaded.events.SkyWarsMatchStateChangeEvent;
import com.walrusone.skywarsreloaded.game.cages.*;
import com.walrusone.skywarsreloaded.game.signs.SWRSign;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.managers.worlds.FileWorldManager;
import com.walrusone.skywarsreloaded.managers.worlds.SWMWorldManager;
import com.walrusone.skywarsreloaded.managers.worlds.WorldManager;
import com.walrusone.skywarsreloaded.matchevents.*;
import com.walrusone.skywarsreloaded.menus.ArenaMenu;
import com.walrusone.skywarsreloaded.menus.ArenasMenu;
import com.walrusone.skywarsreloaded.menus.TeamSelectionMenu;
import com.walrusone.skywarsreloaded.menus.TeamSpectateMenu;
import com.walrusone.skywarsreloaded.menus.gameoptions.*;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.*;
import java.util.logging.Level;

public class GameMap {

    private static ArrayList<GameMap> arenas;

    static {
        new ArenasMenu();
    }

    static {
        GameMap.arenas = new ArrayList<>();
    }

    // In single mode:
    //      All team spawn locations are in team index 0 (aka spawnLocations.get(0))
    // In teams mode:
    //      Key Integer is team index, List is the spawn locations of such team
    public HashMap<Integer, List<CoordLoc>> spawnLocations = new HashMap<>();
    private final ArrayList<Crate> crates = new ArrayList<>();
    private boolean forceStart;
    private boolean disableDamage = false;
    private boolean allowFallDamage;
    private boolean allowRegen;
    private boolean projectilesOnly;
    private boolean projectileSpleefEnabled;
    private boolean doubleDamageEnabled;
    private boolean thunder;
    private boolean allowFriendlyFire;
    private boolean allowScanvenger;
    // Player data
    private final ArrayList<TeamCard> teamCards;
    private ArrayList<UUID> waitingPlayers = Lists.newArrayList();
    private final ArrayList<UUID> spectators = new ArrayList<>();
    private final List<String> winners = new ArrayList<>();
    private final HashMap<Player, Integer> playerKills = new HashMap<>();
    private final ArrayList<String> deathMatchWaiters = new ArrayList<>();
    // ..
    private int strikeCounter;
    private int nextStrike;
    private MatchState matchState;
    private int teamSize;
    // Edit mode settings
    private ChestPlacementType chestPlacementType;
    private final String name;
    private int timer;
    private int minPlayers;
    private GameKit kit;
    private Cage cage;
    // Map settings
    private String currentTime;
    private String currentHealth;
    private String currentChest;
    private String currentWeather;
    private String currentModifier;
    private KitVoteOption kitVoteOption;
    private ChestOption chestOption;
    private HealthOption healthOption;
    private TimeOption timeOption;
    private WeatherOption weatherOption;
    private ModifierOption modifierOption;
    private ArrayList<CoordLoc> chests;
    private ArrayList<CoordLoc> centerChests;
    private String environment;
    private Vote defaultChestType;
    private Vote defaultHealth;
    private Vote defaultTime;
    private Vote defaultWeather;
    private Vote defaultModifier;
    // Map descriptions
    private String displayName;
    private String designedBy;
    private final ArrayList<SWRSign> signs;
    private GameBoard gameboard;
    private boolean registered;
    private final String arenakey;
    private final GameQueue joinQueue;
    private boolean inEditing = false;
    private CoordLoc spectateSpawn;
    private CoordLoc lookDirection;
    private final ArrayList<CoordLoc> deathMatchSpawns;
    private boolean legacy = false;
    private final ArrayList<MatchEvent> events = new ArrayList<>();
    private final ArrayList<String> anvils = new ArrayList<>();
    private boolean customJoinMenuIcon = false;
    private ItemStack customJoinMenuItem = null;
    private CoordLoc waitingLobbySpawn;

    public GameMap(final String name) {
        this.name = name;
        this.matchState = MatchState.OFFLINE;
        teamCards = new ArrayList<>();
        deathMatchSpawns = new ArrayList<>();
        signs = new ArrayList<SWRSign>();
        chests = new ArrayList<>();
        centerChests = new ArrayList<>();
        chestPlacementType = ChestPlacementType.NORMAL;
        loadArenaData();
        this.thunder = false;
        allowRegen = true;
        projectilesOnly = false;
        projectileSpleefEnabled = false;
        doubleDamageEnabled = false;
        timer = SkyWarsReloaded.getCfg().getWaitTimer();
        joinQueue = new GameQueue(this);
        arenakey = name + "menu";
        if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
            kitVoteOption = new KitVoteOption(this, name + "kitVote");
        }
        chestOption = new ChestOption(this, name + "chest");
        healthOption = new HealthOption(this, name + "health");
        timeOption = new TimeOption(this, name + "time");
        weatherOption = new WeatherOption(this, name + "weather");
        modifierOption = new ModifierOption(this, name + "modifier");

        gameboard = new GameBoard(this);
        if (legacy) {
            boolean loaded = loadWorldForScanning(name);
            if (loaded) {
                ChunkIterator(false, null);
                saveArenaData();
                SkyWarsReloaded.getWM().deleteWorld(name, false);
            }
        }
        if (registered) {
            registerMap();
        }

        new ArenaMenu(arenakey, this);
        if (SkyWarsReloaded.getCfg().joinMenuEnabled()) {
            new TeamSelectionMenu(this);
        }
        if (SkyWarsReloaded.getCfg().spectateMenuEnabled()) {
            new TeamSpectateMenu(this);
        }
    }

    public static GameMap getMap(final String mapName) {
        shuffle();
        for (final GameMap map : GameMap.arenas) {
            if (map.name.equalsIgnoreCase(ChatColor.stripColor(mapName))) {
                return map;
            }
        }
        return null;
    }

    private static GameMap addMap(String name) {
        GameMap gMap = new GameMap(name);
        arenas.add(gMap);
        return gMap;
    }

    public static void loadMaps() {
        File mapFile = new File(SkyWarsReloaded.get().getDataFolder(), "maps.yml");
        if (mapFile.exists()) {
            updateMapData();
        }
        arenas.clear();

        if (SkyWarsReloaded.getWM() instanceof FileWorldManager) {
            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
            File maps = new File(dataDirectory, "maps");
            if (maps.exists() && maps.isDirectory()) {
                File[] files = maps.listFiles();
                if (files != null) {
                    for (File map : files) {
                        if (map.isDirectory()) {
                            addMap(map.getName());
                        }
                    }
                }
            } else {
                SkyWarsReloaded.get().getLogger().info("Maps directory is missing or no Maps were found!");
            }
        } else {
            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
            File maps = new File(dataDirectory, "mapsData");
            if (maps.exists() && maps.isDirectory()) {
                File[] files = maps.listFiles();
                if (files != null) {
                    for (File file : files) {
                        addMap(file.getName().replace(".yml", ""));
                    }
                }
            }
        }
    }

    private static void updateMapData() {
        File mapFile = new File(SkyWarsReloaded.get().getDataFolder(), "maps.yml");
        if (mapFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(mapFile);

            if (storage.getConfigurationSection("maps") != null) {
                for (String key : storage.getConfigurationSection("maps").getKeys(false)) {
                    String displayname = storage.getString("maps." + key + ".displayname");
                    int minplayers = storage.getInt("maps." + key + ".minplayers");
                    String creator = storage.getString("maps." + key + ".creator");
                    List<String> signs = storage.getStringList("maps." + key + ".signs");
                    boolean registered = storage.getBoolean("maps." + key + ".registered");

                    File dataDirectory = SkyWarsReloaded.get().getDataFolder();
                    File mapDataDirectory = new File(dataDirectory, "mapsData");

                    if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
                        return;
                    }

                    File newMapFile = new File(mapDataDirectory, key + ".yml");
                    copyDefaults(newMapFile);
                    FileConfiguration fc = YamlConfiguration.loadConfiguration(newMapFile);
                    fc.set("displayname", displayname);
                    fc.set("minplayers", minplayers);
                    fc.set("creator", creator);
                    fc.set("signs", signs);
                    fc.set("registered", registered);
                    fc.set("environment", "NORMAL");
                    fc.set("spectateSpawn", "0:95:0");
                    fc.set("deathMatchSpawns", null);
                    fc.set("legacy", true);
                    try {
                        fc.save(newMapFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            boolean result = mapFile.delete();
            if (!result) {
                SkyWarsReloaded.get().getLogger().info("Failed to Delete Old MapData File");
            }
        }
    }

    /*Player Handling Methods*/

    public static World createNewMap(String mapName, World.Environment environment) {
        World newWorld = SkyWarsReloaded.getWM().createEmptyWorld(mapName, environment);
        if (newWorld == null) {
            return null;
        }
        addMap(mapName);
        GameMap map = GameMap.getMap(mapName);
        if (map != null) {
            map.environment = environment.toString();
            map.saveArenaData();
        }
        return newWorld;
    }

    private static boolean loadWorldForScanning(String name) {
        WorldManager wm = SkyWarsReloaded.getWM();

        if (wm instanceof FileWorldManager) {
            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
            File maps = new File(dataDirectory, "maps");

            String root = SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath();
            File rootDirectory = new File(root);
            File source = new File(maps, name);
            File target = new File(rootDirectory, name);
            wm.copyWorld(source, target);
            boolean mapExists = false;
            if (target.isDirectory()) {
                String[] list = target.list();
                if (list != null && list.length > 0) {
                    mapExists = true;
                }
            }
            if (mapExists) {
                SkyWarsReloaded.getWM().deleteWorld(name, false);
            }

            wm.copyWorld(source, target);
        }

        final boolean[] loaded = {false};
        Bukkit.getScheduler().runTaskAsynchronously(SkyWarsReloaded.get(), () -> {
            loaded[0] = SkyWarsReloaded.getWM().loadWorld(name, World.Environment.NORMAL);
            if (!loaded[0]) {
                SkyWarsReloaded.get().getLogger().info("Could Not Load Map: " + name);
            }
        });

        return loaded[0];
    }

    public static ArrayList<GameMap> getMaps() {
        return new ArrayList<>(arenas);
    }

    public static ArrayList<GameMap> getPlayableArenas(GameType type) {
        ArrayList<GameMap> sorted = new ArrayList<>();
        if (type == GameType.TEAM) {
            for (GameMap gMap : arenas) {
                if (gMap.isRegistered() && gMap.teamSize > 1) {
                    sorted.add(gMap);
                }
            }
        } else if (type == GameType.SINGLE) {
            for (GameMap gMap : arenas) {
                if (gMap.isRegistered() && gMap.teamSize == 1) {
                    sorted.add(gMap);
                }
            }
        } else {
            for (GameMap gMap : arenas) {
                if (gMap.isRegistered()) {
                    sorted.add(gMap);
                }
            }
        }
        sorted.sort(new GameMapComparator());
        return sorted;
    }

    public static ArrayList<GameMap> getSortedArenas() {
        ArrayList<GameMap> sorted = new ArrayList<>(arenas);
        sorted.sort(new GameMapComparator());
        return sorted;
    }

    public static void editMap(GameMap gMap, Player player) {
        if (gMap.isRegistered()) {
            gMap.unregister(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    startEdit(gMap, player);
                }
            }.runTaskLater(SkyWarsReloaded.get(), 20);
        } else {
            startEdit(gMap, player);
        }
    }

    private static void startEdit(GameMap gMap, Player player) {
        String worldName = gMap.getName();
        if (gMap.isEditing()) {
            boolean loaded = false;
            for (World world : SkyWarsReloaded.get().getServer().getWorlds()) {
                if (world.getName().equals(worldName)) {
                    loaded = true;
                }
            }
            if (!loaded) {
                loaded = loadWorld(worldName, gMap);
            }
            if (loaded) {
                prepareForEditor(player, gMap, worldName);
            }
        } else {
            gMap.setEditing(true);
            boolean loaded = loadWorld(worldName, gMap);
            if (loaded) {
                prepareForEditor(player, gMap, worldName);
            } else {
                player.sendMessage(new Messaging.MessageFormatter().format("error.map-fail-load"));
            }
        }
    }

    private static boolean loadWorld(String worldName, GameMap gMap) {
        File dataDirectory = new File(SkyWarsReloaded.get().getDataFolder(), "maps");
        File source = new File(dataDirectory, worldName);
        File target = new File(SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath(), worldName);
        boolean mapExists = false;
        if (target.isDirectory()) {
            String[] list = target.list();
            if (list != null && list.length > 0) {
                mapExists = true;
            }
        }
        if (mapExists) {
            SkyWarsReloaded.getWM().deleteWorld(worldName, false);
        }
        SkyWarsReloaded.getWM().copyWorld(source, target);
        return SkyWarsReloaded.getWM().loadWorld(worldName, World.Environment.valueOf(gMap.environment));
    }

    private static void prepareForEditor(Player player, GameMap gMap, String worldName) {
        World editWorld = SkyWarsReloaded.get().getServer().getWorld(worldName);
        editWorld.setAutoSave(true);
        for (TeamCard tCard : gMap.getTeamCards()) {
            if (tCard.getSpawns() != null) {
                for (CoordLoc loc : tCard.getSpawns()) {
                    editWorld.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setType(Material.DIAMOND_BLOCK);
                }
            }
        }
        for (CoordLoc cl : gMap.getDeathMatchSpawns()) {
            editWorld.getBlockAt(cl.getX(), cl.getY(), cl.getZ()).setType(Material.EMERALD_BLOCK);
        }
        SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> {
            player.teleport(new Location(editWorld, 0, 95, 0), TeleportCause.PLUGIN);
            player.setGameMode(GameMode.CREATIVE);
            player.setAllowFlight(true);
            player.setFlying(true);
        }, 20);
    }

    /*Map Handling Methods*/

    public static void openArenasManager(Player player) {
        if (player.hasPermission("sw.arenas")) {
            SkyWarsReloaded.getIC().show(player, "arenasmenu");
            updateArenasManager();
        }
    }

    public static void updateArenasManager() {
        if (SkyWarsReloaded.getIC().has("arenasmenu")) {
            SkyWarsReloaded.getIC().getMenu("arenasmenu").update();
        }
    }

    public static void shuffle() {
        Collections.shuffle(arenas);
    }

    public static GameMap getMapByDisplayName(String name) {
        for (GameMap gMap : arenas) {
            if (ChatColor.stripColor((ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()))).equalsIgnoreCase(name)) {
                return gMap;
            }
        }
        return null;
    }

    private static void copyDefaults(File mapFile) {
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(mapFile);
        Reader defConfigStream = new InputStreamReader(SkyWarsReloaded.get().getResource("mapFile.yml"));
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        playerConfig.options().copyDefaults(true);
        playerConfig.setDefaults(defConfig);
        try {
            playerConfig.save(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scanWorld(boolean b, Player player) {
        if (inEditing) {
            ChunkIterator(b, player);
            saveArenaData();
        } else {
            GameMap.editMap(this, player);
            ChunkIterator(b, player);
            saveArenaData();
        }
    }

    public void checkSpectators() {
        if (!spectators.isEmpty()) {
            for (UUID uuid : spectators) {
                Player spec = SkyWarsReloaded.get().getServer().getPlayer(uuid);
                if (spec == null) continue;
                if (isOutsideBorder(spec)) {
                    CoordLoc ss = getSpectateSpawn();
                    Location spectateSpawn = new Location(getCurrentWorld(), ss.getX(), ss.getY(), ss.getZ());
                    spec.teleport(spectateSpawn, TeleportCause.END_PORTAL);
                }
            }
        }
    }

    private boolean isOutsideBorder(Player p) {
        Location loc = p.getLocation();
        WorldBorder border = p.getWorld().getWorldBorder();
        double size = SkyWarsReloaded.getCfg().getBorderSize() / 2;
        Location center = border.getCenter();
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }

    private void loadEvents() {
        events.clear();
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return;
        }

        File mapFile = new File(mapDataDirectory, name + ".yml");
        FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
        events.add(new DisableRegenEvent(this, fc.getBoolean("events.DisableRegenEvent.enabled")));
        events.add(new HealthDecayEvent(this, fc.getBoolean("events.HealthDecayEvent.enabled")));
        events.add(new EnderDragonEvent(this, fc.getBoolean("events.EnderDragonEvent.enabled")));
        events.add(new WitherEvent(this, fc.getBoolean("events.WitherEvent.enabled")));
        events.add(new MobSpawnEvent(this, fc.getBoolean("events.MobSpawnEvent.enabled")));
        events.add(new ChestRefillEvent(this, fc.getBoolean("events.ChestRefillEvent.enabled")));
        events.add(new DeathMatchEvent(this, fc.getBoolean("events.DeathMatchEvent.enabled")));
        events.add(new ArrowRainEvent(this, fc.getBoolean("events.ArrowRainEvent.enabled")));
        events.add(new AnvilRainEvent(this, fc.getBoolean("events.AnvilRainEvent.enabled")));
        events.add(new CrateDropEvent(this, fc.getBoolean("events.CrateDropEvent.enabled")));
        events.add(new ShrinkingBorderEvent(this, fc.getBoolean("events.ShrinkingBorderEvent.enabled")));
        events.add(new ProjectilesOnlyEvent(this, fc.getBoolean("events.ProjectilesOnlyEvent.enabled")));
        events.add(new ProjectileSpleefEvent(this, fc.getBoolean("events.ProjectileSpleefEvent.enabled")));
        events.add(new DoubleDamageEvent(this, fc.getBoolean("events.DoubleDamageEvent.enabled")));
        events.add(new GhastEvent(this, fc.getBoolean("events.GhastEvent.enabled")));
    }

    public void update() {
        updateArenasManager();
        this.updateArenaManager();
        this.updateSigns();
        this.sendBungeeUpdate();
        if (SkyWarsReloaded.getIC().has("joinsinglemenu") && teamSize == 1) {
            SkyWarsReloaded.getIC().getMenu("joinsinglemenu").update();
        }
        if (SkyWarsReloaded.getIC().has("jointeammenu") && teamSize > 1) {
            SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
            if ((matchState == MatchState.WAITINGSTART || matchState == MatchState.WAITINGLOBBY) && SkyWarsReloaded.getIC().has(name + "teamselect")) {
                SkyWarsReloaded.getIC().getMenu(name + "teamselect").update();
            }
        }
    }

    public boolean addPlayers(@Nullable TeamCard teamToTry, final Player player) {
        // IF busy return false
        if (Util.get().isBusy(player.getUniqueId())) {
            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                Bukkit.getLogger().log(Level.WARNING, "#addPlayers: " + player.getName() + " is busy, cannot join " + this.getName());
            }
            return false;
        }
        boolean result = false;
        PlayerStat ps = PlayerStat.getPlayerStats(player.getUniqueId());
        if (ps == null || !ps.isInitialized()) return false;

        if (teamSize > 1) {
            teamCards.sort(new TeamCardComparator());
        } else {
            Collections.shuffle(teamCards);
        }
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            Bukkit.getLogger().log(Level.WARNING, "#addPlayers: " + player.getName() + "'s PlayerStats are initialized");
            Bukkit.getLogger().log(Level.WARNING, "#addPlayers: MatchState: " + getMatchState().name());
        }
        // If in any mode & WAITING while countdown
        if (getMatchState() == MatchState.WAITINGSTART ) {
            TeamCard reservedTeamCard = null;
            if (teamToTry == null) { // If not in party mode
                for (TeamCard tCard : teamCards) {
                    if (SkyWarsReloaded.getCfg().debugEnabled()) {
                        Bukkit.getLogger().log(Level.WARNING, "#addPlayers: --teamCard: " + (tCard.getPlace() + 1));
                        Bukkit.getLogger().log(Level.WARNING, "#addPlayers: (" + (tCard.getPlace() + 1) + ") fullCount: " + tCard.getFullCount());
                    }
                    if (tCard.getFullCount() > 0) { // If space available
                        Util.get().ejectPassengers(player);
                        reservedTeamCard = tCard.sendReservation(player, ps);
                        break;
                    }
                }
            } else { // In party mode
                if (teamToTry.getFullCount() > 0) {
                    Util.get().ejectPassengers(player);
                    reservedTeamCard = teamToTry.sendReservation(player, ps);
                }
            }
            if (reservedTeamCard != null) { // If setup for player success
                Util.get().ejectPassengers(player);
                result = reservedTeamCard.joinGame(player); // tp to arena
                if (result) {
                    PlayerStat.resetScoreboard(player);
                }
            } else { // Warn console that setup failed
                SkyWarsReloaded.get().getLogger().warning("Failed to send reservation for " + player.getName());
            }
            // else if in lobby waiting mode
        } else if (getMatchState() == MatchState.WAITINGLOBBY) {
            Util.get().ejectPassengers(player);
            PlayerStat.resetScoreboard(player);
            addWaitingPlayer(player);
            getJoinQueue().add(new PlayerCard(null, player.getUniqueId(), null));
            Bukkit.getPluginManager().callEvent(new SkyWarsJoinEvent(player, this));
            if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
                getKitVoteOption().updateKitVotes();
            }
            if (SkyWarsReloaded.getCfg().resetTimerOnJoin()) {
                setTimer(SkyWarsReloaded.getCfg().getWaitTimer());
            }
            result = true;
        }
        if (SkyWarsReloaded.getCfg().debugEnabled()) {
            Bukkit.getLogger().info("#addPlayers: result = " + result);
        }
        this.update();
        return result;
    }

    public boolean addPlayers(@Nullable TeamCard teamToTry, final Party party) {
        TeamCard team = null;
        Map<TeamCard, ArrayList<Player>> players = new HashMap<>();
        if (teamSize == 1) {
            Collections.shuffle(teamCards);
            for (UUID uuid : party.getMembers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (Util.get().isBusy(uuid)) {
                    party.sendPartyMessage(new Messaging.MessageFormatter().setVariable("player", player.getName()).format("party.memberbusy"));
                } else {
                    PlayerStat ps = PlayerStat.getPlayerStats(uuid);
                    if (ps != null && player != null && ps.isInitialized()) {
                        for (TeamCard tCard : teamCards) {
                            if (tCard.getFullCount() > 0) {
                                Util.get().ejectPassengers(player);
                                TeamCard reserve = tCard.sendReservation(player, ps);
                                if (reserve != null) {
                                    players.computeIfAbsent(reserve, k -> new ArrayList<>());
                                    players.get(reserve).add(player);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            if (teamToTry == null) {
                teamCards.sort(new TeamCardComparator());
                for (TeamCard tCard : teamCards) {
                    if (tCard.getFullCount() >= party.getSize()) {
                        for (int i = 0; i < party.getSize(); i++) {
                            Player player = Bukkit.getPlayer(party.getMembers().get(i));
                            PlayerStat ps = PlayerStat.getPlayerStats(player.getUniqueId());
                            if (ps != null && ps.isInitialized()) {
                                Util.get().ejectPassengers(player);
                                TeamCard reserve = tCard.sendReservation(player, ps);
                                if (reserve != null) {
                                    players.computeIfAbsent(reserve, k -> new ArrayList<>()).add(player);
                                }
                                team = reserve;
                            }
                        }
                    }
                    if (team != null && players.get(team).size() == party.getSize()) {
                        break;
                    }
                }
            } else {
                if (teamToTry.getFullCount() >= party.getSize()) {
                    for (int i = 0; i < party.getSize(); i++) {
                        Player player = Bukkit.getPlayer(party.getMembers().get(i));
                        PlayerStat ps = PlayerStat.getPlayerStats(player.getUniqueId());
                        if (ps != null && ps.isInitialized()) {
                            Util.get().ejectPassengers(player);
                            TeamCard reserve = teamToTry.sendReservation(player, ps);
                            if (reserve != null) {
                                players.computeIfAbsent(reserve, k -> new ArrayList<>()).add(player);
                            }
                            team = reserve;
                        }
                    }
                }
            }
        }

        boolean result = false;
        if (teamSize == 1 && players.size() == party.getSize()) {
            for (TeamCard tCard : players.keySet()) {
                result = tCard.joinGame(players.get(tCard).get(0));
            }
        } else if (teamSize > 1 && team != null && players.get(team).size() == party.getSize()) {
            for (int i = 0; i < players.get(team).size(); i++) {
                result = team.joinGame(players.get(team).get(i));
            }
        } else {
            for (ArrayList<Player> play : players.values()) {
                for (Player player : play) {
                    PlayerCard pCard = this.getPlayerCard(player);
                    pCard.reset();
                }
            }
        }
        this.update();
        gameboard.updateScoreboard();
        return result;
    }

    public void removePlayer(final UUID uuid) {
        for (TeamCard tCard : teamCards) {
            if (tCard.removePlayer(uuid)) break;
        }
        spectators.remove(uuid);
        waitingPlayers.remove(uuid);
        this.update();
        gameboard.updateScoreboard();
    }

    public ArrayList<Player> getAlivePlayers() {
        ArrayList<Player> alivePlayers = new ArrayList<>();
        for (TeamCard tCard : teamCards) {
            for (PlayerCard pCard : tCard.getPlayerCards()) {
                Player cardPlayer = pCard.getPlayer();
                if (cardPlayer != null) {
                    if (!mapContainsDead(cardPlayer.getUniqueId()) && cardPlayer.getGameMode() != GameMode.SPECTATOR) {
                        alivePlayers.add(cardPlayer);
                    }
                }
            }
        }
        return alivePlayers;
    }

    public boolean mapContainsDead(UUID uuid) {
        for (TeamCard tCard : teamCards) {
            if (tCard.getDead().contains(uuid)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> allPlayers = new ArrayList<>();
        for (TeamCard tCard : teamCards) {
            for (PlayerCard pCard : tCard.getPlayerCards()) {
                if (pCard.getPlayer() != null) {
                    allPlayers.add(pCard.getPlayer());
                }
            }
        }
        for (UUID player : waitingPlayers) {
            Player pl = Bukkit.getPlayer(player);
            if (pl == null) continue;
            if (!allPlayers.contains(pl)) {
                allPlayers.add(pl);
            }
        }
        for (UUID player : spectators) {
            Player pl = Bukkit.getPlayer(player);
            if (pl == null) continue;
            if (!allPlayers.contains(pl)) {
                allPlayers.add(pl);
            }
        }
        return allPlayers;
    }

    public ArrayList<Player> getMessageAblePlayers(boolean isSpectator) {
        ArrayList<Player> receivers = new ArrayList<>();
        if (!isSpectator) {
            for (TeamCard tCard : teamCards) {
                for (PlayerCard pCard : tCard.getPlayerCards()) {
                    if (pCard.getPlayer() != null) {
                        if (!mapContainsDead(pCard.getPlayer().getUniqueId())) {
                            receivers.add(pCard.getPlayer());
                        }
                    }
                }
            }
            for (UUID pUid : waitingPlayers) {
                if (!mapContainsDead(pUid)) {
                    Player p = Bukkit.getPlayer(pUid);
                    if (p != null && !receivers.contains(p)) {
                        receivers.add(p);
                    }
                }
            }
        }
        for (UUID uuid : spectators) {
            Player player = SkyWarsReloaded.get().getServer().getPlayer(uuid);
            if (player != null) {
                receivers.add(player);
            }
        }
        return receivers;
    }

    public boolean canAddPlayer() {
        if (!((this.matchState == MatchState.WAITINGSTART || this.matchState == MatchState.WAITINGLOBBY) && this.registered)) {
            return false;
        }
        for (TeamCard tCard : teamCards) {
            if (tCard.getFullCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean canAddParty(Party party) {
        if (!((this.matchState == MatchState.WAITINGSTART || this.matchState == MatchState.WAITINGLOBBY) && this.registered)) {
            return false;
        }
        if (teamSize == 1) {
            int playerCount = getPlayerCount();
            return playerCount + party.getSize() - 1 < teamCards.size();
        } else {
            for (TeamCard tCard : teamCards) {
                if (tCard.getFullCount() >= party.getSize()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeMap() {
        unregister(false);
        File dataDirectory = new File(SkyWarsReloaded.get().getDataFolder(), "maps");
        File target = new File(dataDirectory, name);
        SkyWarsReloaded.getWM().deleteWorld(target);
        if (SkyWarsReloaded.getWM() instanceof SWMWorldManager) SkyWarsReloaded.getWM().deleteWorld(name, true);

        File mapDataDirectory = new File(SkyWarsReloaded.get().getDataFolder(), "mapsData");
        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return false;
        }
        File mapFile = new File(mapDataDirectory, name + ".yml");
        boolean result = mapFile.delete();
        if (result) {
            arenas.remove(this);
        }
        return result;
    }

    private void saveArenaData() {
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return;
        }

        File mapFile = new File(mapDataDirectory, name + ".yml");
        if (!mapFile.exists()) {
            SkyWarsReloaded.get().getLogger().info("File doesn't exist!");
            return;
        }
        copyDefaults(mapFile);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
        fc.set("displayname", displayName);
        fc.set("minplayers", minPlayers);
        fc.set("creator", designedBy);
        fc.set("registered", registered);
        fc.set("spectateSpawn", spectateSpawn.getLocation());
        fc.set("cage", cage.getType().toString().toLowerCase());
        fc.set("teamSize", teamSize);
        fc.set("environment", environment);
        fc.set("defaultChestType", defaultChestType.name());
        fc.set("defaultHealth", defaultHealth.name());
        fc.set("defaultTime", defaultTime.name());
        fc.set("defaultWeather", defaultWeather.name());
        fc.set("defaultModifier", defaultModifier.name());
        fc.set("allowFriendlyFire", allowFriendlyFire);
        fc.set("allowScavenger", allowScanvenger);

        fc.set("enableCustomJoinMenuItem", customJoinMenuIcon);

        if (waitingLobbySpawn != null) {
            fc.set("waitingLobbySpawn", waitingLobbySpawn.getLocation());
        }

        if (teamSize == 1) {
            List<String> spawns = new ArrayList<>();
            if (spawnLocations.size() >= 1 && spawnLocations.containsKey(0)) {
                for (CoordLoc loc : spawnLocations.get(0)) {
                    spawns.add(loc.getLocation());
                }
            }
            fc.set("spawns", spawns);
        } else {
            for (int teamNumber : spawnLocations.keySet()) {

                List<String> spawns = new ArrayList<>();
                List<CoordLoc> locs = spawnLocations.get(teamNumber);
                for (CoordLoc loc : locs) {
                    spawns.add(loc.getLocation());
                }

                fc.set("spawns.team-" + teamNumber, spawns);
            }
        }

        List<String> dSpawns = new ArrayList<>();
        for (CoordLoc loc : deathMatchSpawns) {
            dSpawns.add(loc.getLocation());
        }
        fc.set("deathMatchSpawns", dSpawns);

        List<String> stringSigns = new ArrayList<>();
        for (SWRSign s : signs) {
            stringSigns.add(Util.get().locationToString(s.getLocation()));
        }
        fc.set("signs", stringSigns);

        List<String> stringChests = new ArrayList<>();
        for (CoordLoc chest : chests) {
            stringChests.add(chest.getLocation());
        }
        fc.set("chests", stringChests);

        List<String> stringCenterChests = new ArrayList<>();
        for (CoordLoc chest : centerChests) {
            stringCenterChests.add(chest.getLocation());
        }
        fc.set("centerChests", stringCenterChests);

        fc.set("legacy", null);
        try {
            fc.save(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getCustomJoinMenuItemEnabled() {
        return customJoinMenuIcon;
    }

    public ItemStack getCustomJoinMenuItem() {
        return customJoinMenuItem;
    }

    public void loadArenaData() {
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File mapDataDirectory = new File(dataDirectory, "mapsData");

        if (!mapDataDirectory.exists() && !mapDataDirectory.mkdirs()) {
            return;
        }

        File mapFile = new File(mapDataDirectory, name + ".yml");
        copyDefaults(mapFile);

        // Start parse options
        FileConfiguration fc = YamlConfiguration.loadConfiguration(mapFile);
        displayName = fc.getString("displayname", name);
        designedBy = fc.getString("creator", "");
        registered = fc.getBoolean("registered", false);
        spectateSpawn = Util.get().getCoordLocFromString(fc.getString("spectateSpawn", "0:95:0"));
        lookDirection = Util.get().getCoordLocFromString(fc.getString("lookDirection", "00:95:0"));
        legacy = fc.getBoolean("legacy");
        teamSize = fc.getInt("teamSize", 1);
        environment = fc.getString("environment", "NORMAL");
        allowFriendlyFire = fc.getBoolean("allowFriendlyFire", false);
        allowScanvenger = fc.getBoolean("allowScavenger", false);

        // Default Vote Types
        String cfgDefaultChestType = fc.getString("defaultChestType", "CHESTNORMAL");
        Bukkit.getConsoleSender().sendMessage("Default chest type of map " + name + " is " + cfgDefaultChestType);
        if (cfgDefaultChestType.startsWith("CHEST")) {
            defaultChestType = Vote.getByValue(cfgDefaultChestType, Vote.CHESTNORMAL);
            Bukkit.getConsoleSender().sendMessage("Default chest type of map" + name + " started with CHEST and is set to " + defaultChestType);
        } else defaultChestType = Vote.CHESTNORMAL;

        String cfgDefaultHealth = fc.getString("defaultHealth", "HEALTHTEN");
        if (cfgDefaultHealth.startsWith("HEALTH"))
            defaultHealth = Vote.getByValue(cfgDefaultHealth, Vote.HEALTHTEN);
        else defaultHealth = Vote.HEALTHTEN;
        String cfgDefaultTime = fc.getString("defaultTime", "TIMENOON");
        if (cfgDefaultTime.startsWith("TIME"))
            defaultTime = Vote.getByValue(cfgDefaultTime, Vote.TIMENOON);
        else defaultTime = Vote.TIMENOON;
        String cfgDefaultWeather = fc.getString("defaultWeather", "WEATHERSUN");
        if (cfgDefaultWeather.startsWith("WEATHER"))
            defaultWeather = Vote.getByValue(cfgDefaultWeather, Vote.WEATHERSUN);
        else defaultWeather = Vote.WEATHERSUN;
        String cfgDefaultModifier = fc.getString("defaultModifier", "MODIFIERNONE");
        if (cfgDefaultModifier.startsWith("MODIFIER"))
            defaultModifier = Vote.getByValue(cfgDefaultModifier, Vote.MODIFIERNONE);
        else defaultModifier = Vote.MODIFIERNONE;

        // Icon
        customJoinMenuIcon = fc.getBoolean("enableCustomJoinMenuItem", false);
        SkyWarsReloaded.getIM().addExtraItem("joinitem-" + name, new ArrayList<>(), fc.getString("customJoinMenuItem", "LEATHER_HELMET"));
        customJoinMenuItem = SkyWarsReloaded.getIM().getItem("joinitem-" + name);

        String cage = fc.getString("cage");
        CageType ct = CageType.matchType(cage.toUpperCase());
        setCage(ct);

        List<String> dSpawns = fc.getStringList("deathMatchSpawns");
        List<String> stringSigns = fc.getStringList("signs");
        List<String> stringChests = fc.getStringList("chests");
        List<String> stringCenterChests = fc.getStringList("centerChests");

        teamCards.clear();
        spawnLocations.clear();

        if (teamSize == 1) {
            List<String> spawns = fc.getStringList("spawns");
            List<CoordLoc> lls = Lists.newArrayList();
            for (int i = 0; i < spawns.size(); i++) {
                lls.add(Util.get().getCoordLocFromString(spawns.get(i)));
                spawnLocations.remove(0);
                spawnLocations.put(0, lls);
                addTeamCard(i);
            }

        } else {
            if (!(fc.get("spawns") instanceof List)) {
                for (String team : fc.getConfigurationSection("spawns").getKeys(false)) {
                    if (team.startsWith("team-")) {
                        List<String> spawns = fc.getStringList("spawns." + team);
                        List<CoordLoc> locs = Lists.newArrayList();
                        for (String spawn : spawns) {
                            locs.add(Util.get().getCoordLocFromString(spawn));
                        }
                        // Remove 1 to change from user readable to program index
                        int teamIndex = Integer.parseInt(team.replace("team-", "")) - 1;
                        spawnLocations.put(teamIndex, locs);
                        addTeamCard(teamIndex);
                    }
                }
            }
        }

        if (fc.contains("waitingLobbySpawn")) {
            waitingLobbySpawn = Util.get().getCoordLocFromString(fc.getString("waitingLobbySpawn"));
        }

        int def = 2;
        if (teamCards.size() > 4) {
            def = teamCards.size() / 2;
        }
        minPlayers = fc.getInt("minplayers", def);

        deathMatchSpawns.clear();
        for (String dSpawn : dSpawns) {
            deathMatchSpawns.add(Util.get().getCoordLocFromString(dSpawn));
        }

        signs.clear();
        for (String s : stringSigns) {
            signs.add(SkyWarsReloaded.getNMS().createSWRSign(name, Util.get().stringToLocation(s)));
        }

        chests.clear();
        for (String chest : stringChests) {
            addChest(Util.get().getCoordLocFromString(chest), ChestPlacementType.NORMAL, false);
        }

        centerChests.clear();
        for (String chest : stringCenterChests) {
            addChest(Util.get().getCoordLocFromString(chest), ChestPlacementType.CENTER, false);
        }

        loadEvents();
        try {
            fc.save(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Code 0 = successfully registered
     * Code 1 = Unbalanced teams
     * Code 2 = Not enough spawns (at least 2)
     * Code 3 = No spectating spawn set
     * Code 4 = No lobby spawn set in team mode
     * Code -1 = Unknown issue
     *
     * @return int status of registering
     */
    public int registerMap() {
        if (inEditing) {
            saveMap(null);
        }
        if (spawnLocations.size() > 1 || (teamSize == 1 && spawnLocations.size() == 1 && spawnLocations.get(0).size() > 1)) {
            int maxPlayers = getMaxPlayers();
            int actualMaxPlayers = teamCards.size() * teamSize;

            if ((teamSize > 1 && maxPlayers == actualMaxPlayers || !SkyWarsReloaded.getCfg().isUseSeparateCages()) || (teamSize == 1 && maxPlayers > 1)) {
                if (spectateSpawn == null && SkyWarsReloaded.getCfg().spectateEnable()) {
                    SkyWarsReloaded.get().getLogger().info("Could Not Register Map: " + name + " - No spectator spawn has been set. Set it using '/swm spawn spec'");
                    registered = false;
                    return 3;
                }
                if (waitingLobbySpawn == null && teamSize > 1) {
                    SkyWarsReloaded.get().getLogger().info("Could Not Register Map: " + name + " - No waiting lobby spawn has been set. This is required for team games. Set it using '/swm spawn lobby'");
                    registered = false;
                    return 4;
                }

                registered = true;
                gameboard = new GameBoard(this);
                refreshMap();
                getJoinQueue().start();
                SkyWarsReloaded.get().getLogger().info("Registered Map " + name + "!");
                return 0;
            } else {
                registered = false;
                SkyWarsReloaded.get().getLogger().info("Could Not Register Map: " + name + " - Not all teams have enough spawns. There are only " + maxPlayers + "/" + actualMaxPlayers + " spawns set.");
                return 1;
            }
        } else {
            registered = false;
            SkyWarsReloaded.get().getLogger().info("Could Not Register Map: " + name + " - Map must have at least 2 Spawn Points");
            return 2;
        }
    }

    /*Inventories*/

    public void unregister(boolean save) {
        if (getJoinQueue() != null) getJoinQueue().kill();
        // Unregister and stop
        this.registered = false;
        stopGameInProgress();
        // If save -> save all arena data
        if (save) {
            saveArenaData();
        }
    }

    public void stopGameInProgress() {
        this.setMatchState(MatchState.OFFLINE);
        ImmutableList<UUID> specUUIDs = ImmutableList.copyOf(this.getSpectators());
        for (final UUID uuid : specUUIDs) {
            final Player player = SkyWarsReloaded.get().getServer().getPlayer(uuid);
            if (player != null) {
                SkyWarsReloaded.get().getPlayerManager().removePlayer(
                        player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, false
                );
            }
        }
        for (final Player player : this.getAlivePlayers()) {
            if (player != null) {
                SkyWarsReloaded.get().getPlayerManager().removePlayer(
                        player, PlayerRemoveReason.OTHER, null, false
                );
            }
        }
        SkyWarsReloaded.getWM().deleteWorld(this.getName(), false);
    }

    private void loadMap() {
        WorldManager wm = SkyWarsReloaded.getWM();
        String mapName = name;
        if (wm instanceof FileWorldManager) {
            boolean mapExists = false;
            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
            File maps = new File(dataDirectory, "maps");
            File source = new File(maps, name);
            String root = SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath();
            File rootDirectory = new File(root);
            File target = new File(rootDirectory, mapName);
            if (target.isDirectory()) {
                String[] list = target.list();
                if (list != null && list.length > 0) {
                    mapExists = true;
                }
            }
            if (mapExists) {
                SkyWarsReloaded.getWM().deleteWorld(mapName, true);
            }
            wm.copyWorld(source, target);
        }


        boolean loaded = SkyWarsReloaded.getWM().loadWorld(mapName, World.Environment.valueOf(environment));

        if (loaded) {
            World world = SkyWarsReloaded.get().getServer().getWorld(mapName);
            world.setSpawnLocation(2000, 0, 2000);
            if (SkyWarsReloaded.getCfg().borderEnabled()) {
                WorldBorder wb = world.getWorldBorder();
                wb.setCenter(teamCards.get(0).getSpawns().get(0).getX(), teamCards.get(0).getSpawns().get(0).getZ());
                wb.setSize(SkyWarsReloaded.getCfg().getBorderSize());
            }


            for (int teamNumber : spawnLocations.keySet()) {
                for (CoordLoc loc : spawnLocations.get(teamNumber)) {
                    getCurrentWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setType(Material.AIR);
                }
            }
            if (teamSize > 1) {
                // todo test this
                cage.createSpawnPlatforms(this);
            }
        }
    }

    private void ChunkIterator(boolean message, Player sender) {
        World chunkWorld;
        chunkWorld = SkyWarsReloaded.get().getServer().getWorld(name);
        int mapSize = SkyWarsReloaded.getCfg().getMaxMapSize();
        int max1 = mapSize / 2;
        int min1 = -mapSize / 2;
        Block min = chunkWorld.getBlockAt(min1, 0, min1);
        Block max = chunkWorld.getBlockAt(max1, 0, max1);
        Chunk cMin = min.getChunk();
        Chunk cMax = max.getChunk();
        teamCards.clear();
        chests.clear();

        List<CoordLoc> soloSpawns;

        for (int cx = cMin.getX(); cx < cMax.getX(); cx++) {
            for (int cz = cMin.getZ(); cz < cMax.getZ(); cz++) {
                Chunk currentChunk = chunkWorld.getChunkAt(cx, cz);
                currentChunk.load(true);

                for (BlockState te : currentChunk.getTileEntities()) {
                    if (te instanceof Beacon) {
                        Beacon beacon = (Beacon) te;
                        Block block = beacon.getBlock().getRelative(0, -1, 0);
                        if (block == null || (!block.getType().equals(Material.GOLD_BLOCK) && !block.getType().equals(Material.IRON_BLOCK)
                                && !block.getType().equals(Material.DIAMOND_BLOCK) && !block.getType().equals(Material.EMERALD_BLOCK))) {
                            Location loc = beacon.getLocation();
                            if (teamSize == 1) {
                                soloSpawns = spawnLocations.getOrDefault(0,Lists.newArrayList());
                                soloSpawns.add(new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                                spawnLocations.put(0, soloSpawns);
                                addTeamCard(soloSpawns.size() - 1);
                                if (message) {
                                    sender.sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + getMaxPlayers()).setVariable("mapname", getDisplayName()).format("maps.addSpawn"));
                                }
                            }
                        }
                        // Add chests found in the world to map chests
                    } else if (te instanceof Chest) {
                        Chest chest = (Chest) te;
                        // If loadTrappedChestsAsCenter option is enabled, we check if the chest is trapped. If so it's a center chest
                        if (SkyWarsReloaded.getCfg().getLoadTrappedChestsAsCenter() &&
                                chest.getType().equals(Material.TRAPPED_CHEST)) {
                            Block trappedChestBlock = chest.getBlock();
                            if (trappedChestBlock != null) {
                                // Replace the trapped chest with a chest to avoid anomalies in-game
                                trappedChestBlock.setType(Material.CHEST);
                                // Add the chest as center
                                addChest(chest, ChestPlacementType.CENTER);
                            }
                        } else {
                            // Add the chest as normal
                            addChest(chest, ChestPlacementType.NORMAL);
                        }
                        if (message) {
                            // Inform the player scanning the map about found chests
                            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", getDisplayName()).format("maps.addChest"));
                        }
                    }
                }
            }
        }
    }

    public void refreshMap() {
        for (TeamCard tCard : teamCards) {
            tCard.reset();
        }
        thunder = false;
        forceStart = false;
        allowRegen = true;
        projectilesOnly = false;
        projectileSpleefEnabled = false;
        doubleDamageEnabled = false;
        kit = null;
        winners.clear();
        deathMatchWaiters.clear();
        waitingPlayers.clear();
        spectators.clear();
        playerKills.clear();
        if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
            kitVoteOption.restore();
        }
        for (MatchEvent event : events) {
            event.reset();
        }
        healthOption.restore();
        chestOption.restore();
        timeOption.restore();
        weatherOption.restore();
        modifierOption.restore();
        gameboard.setRestartTimer(-1);
        SkyWarsReloaded.getWM().deleteWorld(name, false);
        final GameMap gMap = this;
        if (SkyWarsReloaded.get().isEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    gMap.loadMap();
                }
            }.runTaskLater(SkyWarsReloaded.get(), 10);
        }
        if (SkyWarsReloaded.get().isEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (teamSize > 1) {
                        setMatchState(MatchState.WAITINGLOBBY);
                    } else {
                        setMatchState(MatchState.WAITINGSTART);
                    }
                    gameboard.updateScoreboard();
                    MatchManager.get().start(gMap);
                    update();
                }
            }.runTaskLater(SkyWarsReloaded.get(), 50);
        }
    }

    /*Bungeemode Methods*/

    public void updateArenaManager() {
        if (SkyWarsReloaded.getIC().has(arenakey)) {
            SkyWarsReloaded.getIC().getMenu(arenakey).update();
        }
    }

    /*Sign Methods*/

    public void setKitVote(Player player, GameKit kit2) {
        for (TeamCard tCard : teamCards) {
            for (PlayerCard pCard : tCard.getPlayerCards()) {
                if (pCard.getPlayer() != null && pCard.getPlayer().equals(player)) {
                    pCard.setKitVote(kit2);
                    return;
                }
            }
        }
    }

    public GameKit getSelectedKit(Player player) {
        for (TeamCard tCard : teamCards) {
            for (PlayerCard pCard : tCard.getPlayerCards()) {
                if (pCard != null) {
                    if (pCard.getPlayer() != null && pCard.getPlayer().equals(player)) {
                        return pCard.getKitVote();
                    }
                }
            }
        }
        return null;
    }

    private void sendBungeeUpdate() {
        if (SkyWarsReloaded.getCfg().bungeeMode()) {
            String playerCount = "" + this.getAlivePlayers().size();
            String maxPlayers = "" + this.getMaxPlayers();
            String gameStarted = "" + this.matchState.toString();
            ArrayList<String> messages = new ArrayList<>();
            messages.add("ServerUpdate");
            messages.add(SkyWarsReloaded.get().getServerName());
            messages.add(playerCount);
            messages.add(maxPlayers);
            messages.add(gameStarted);
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player != null) {
                SkyWarsReloaded.get().sendSWRMessage(player, SkyWarsReloaded.getCfg().getBungeeLobby(), messages);
            }
        }
    }

    public void updateSigns() {
        for (SWRSign s : signs) {
            s.update();
        }
    }

    public List<SWRSign> getSigns() {
        return this.signs;
    }



    /*Getter and Setter Methods*/

    public boolean hasSign(Location loc) {
        for (SWRSign s : signs) {
            if (s.getLocation().equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeSign(Location loc) {
        SWRSign sign = null;
        for (SWRSign s : signs) {
            if (s.getLocation().equals(loc)) {
                sign = s;
            }
        }
        if (sign != null) {
            signs.remove(sign);
            saveArenaData();
            updateSigns();
            return true;
        }
        return false;
    }

    public void addSign(Location loc) {
        signs.add(SkyWarsReloaded.getNMS().createSWRSign(name, loc));
        saveArenaData();
        updateSigns();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName2) {
        this.displayName = displayName2;
        saveArenaData();
    }

    public String getDesigner() {
        return this.designedBy;
    }

    public ArrayList<CoordLoc> getChests() {
        return chests;
    }

    public ArrayList<CoordLoc> getCenterChests() {
        return centerChests;
    }

    public Vote getDefaultChestType() {
        return defaultChestType;
    }

    public Vote getDefaultHealth() {
        return defaultHealth;
    }

    public Vote getDefaultTime() {
        return defaultTime;
    }

    public Vote getDefaultWeather() {
        return defaultWeather;
    }

    public Vote getDefaultModifier() {
        return defaultModifier;
    }

    public MatchState getMatchState() {
        return this.matchState;
    }

    public void setMatchState(final MatchState state) {
        Bukkit.getPluginManager().callEvent(new SkyWarsMatchStateChangeEvent(this, state));
        this.matchState = state;
    }

    public int getPlayerCount() {
        int count = 0;
        for (TeamCard tCard : teamCards) {
            for (PlayerCard pCard : tCard.getPlayerCards()) {
                if (pCard.getPlayer() != null) count++;
            }
        }
        return count;
    }

    public int getMinTeams() {
        if (minPlayers == 0) {
            return teamCards.size();
        }
        return minPlayers;
    }

    public void setMinTeams(int x) {
        minPlayers = x;
        saveArenaData();
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(final int length) {
        this.timer = length;
        if (SkyWarsReloaded.getCfg().isDisplayPlayerExeperience()) {
            if (matchState != MatchState.PLAYING) {
                for (Player player : getAllPlayers()) {
                    player.setLevel(length);
                }
            }
        }
        getGameBoard().updateScoreboard();
    }

    public GameKit getKit() {
        return kit;
    }

    public void setKit(GameKit voted) {
        this.kit = voted;
    }

    public String getName() {
        return this.name;
    }

    public void setAllowFallDamage(boolean b) {
        allowFallDamage = b;
    }

    public boolean allowFallDamage() {
        return allowFallDamage;
    }

    public ArrayList<UUID> getSpectators() {
        return spectators;
    }

    public boolean isThunder() {
        return thunder;
    }

    public int getNextStrike() {
        return nextStrike;
    }

    public void setNextStrike(int randomNum) {
        nextStrike = randomNum;
    }

    public int getStrikeCounter() {
        return strikeCounter;
    }

    public void setStrikeCounter(int num) {
        strikeCounter = num;
    }

    /**
     * Returns the maximum number of players that can join a match
     */
    public int getMaxPlayers() {
        int i = 0;
        for (int a : spawnLocations.keySet()) {
            i += spawnLocations.get(a).size();
        }
        return i;
        //return teamCards.size() * teamSize;
    }

    public void setThunderStorm(boolean b) {
        this.thunder = b;
    }

    public ArrayList<PlayerCard> getPlayerCards() {
        ArrayList<PlayerCard> cards = new ArrayList<>();
        for (TeamCard tCard : teamCards) {
            cards.addAll(tCard.getPlayerCards());
        }
        return cards;
    }

    public PlayerCard getPlayerCard(Player player) {
        for (TeamCard tCard : teamCards) {
            for (PlayerCard pCard : tCard.getPlayerCards()) {
                if (pCard.getPlayer() != null && pCard.getPlayer().equals(player)) {
                    return pCard;
                }
            }
        }
        return null;
    }

    public boolean getForceStart() {
        return forceStart;
    }

    public void setForceStart(boolean state) {
        forceStart = state;
    }

    public void setAllowRegen(boolean b) {
        allowRegen = b;
    }

    public boolean allowRegen() {
        return allowRegen;
    }

    public void addWinner(String name) {
        winners.add(name);
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean b) {
        registered = b;
        saveArenaData();
        update();
    }

    public void setCreator(String creator) {
        this.designedBy = creator;
        saveArenaData();
    }

    public String getArenaKey() {
        return arenakey;
    }

    public GameOption getChestOption() {
        return chestOption;
    }

    public GameOption getTimeOption() {
        return timeOption;
    }

    public GameOption getWeatherOption() {
        return weatherOption;
    }

    public GameOption getModifierOption() {
        return modifierOption;
    }

    public KitVoteOption getKitVoteOption() {
        return kitVoteOption;
    }

    public GameOption getHealthOption() {
        return healthOption;
    }

    public boolean isEditing() {
        return inEditing;
    }

    public void setEditing(boolean b) {
        inEditing = b;
    }

    public World getCurrentWorld() {
        return SkyWarsReloaded.get().getServer().getWorld(name);
    }

    public CoordLoc getLookDirection() {
        return lookDirection;
    }

    public void setLookDirection(Location location) {
        lookDirection = new CoordLoc(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        saveArenaData();
    }

    public void setFriendlyFire(boolean state) {
        allowFriendlyFire = state;
        saveArenaData();
    }

    public void addTeamCard(int teamIndex, boolean saveFile) {
        String prefix = "";
        int teamCardsSize = teamCards.size();

        // Reset if already exists
        teamCards.removeIf(card -> card.getPosition() == teamIndex);

        List<CoordLoc> locs = Lists.newArrayList();
        // Solo mode
        if (teamSize <= 1) {
            if (spawnLocations.size() > 0 && spawnLocations.containsKey(0)) {
                locs.add(spawnLocations.get(0).get(teamIndex));
            }
            // Teams mode
        } else {
            // Set prefix color
            prefix = getChatColor(teamCardsSize);
            locs = spawnLocations.get(teamIndex);
        }
        // Add new team at index maxIndex + 1 which is also size()
        teamCards.add(new TeamCard(teamSize, this, prefix, getStringColor(teamCardsSize), teamCardsSize, locs));
        if (saveFile) {
            saveArenaData();
        }
    }

    /**
     * Add a team slot to the current GameMap
     * @param teamIndex Index of team starting at 0 to ...
     */
    public void addTeamCard(int teamIndex) {
        addTeamCard(teamIndex, false);
    }

    public void addTeamCard(Location loc, int teamIndex) {
        if (teamSize == 1) {
            List<CoordLoc> spawnLocs = spawnLocations.getOrDefault(0, Lists.newArrayList());
            spawnLocs.add(new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            spawnLocations.put(0, spawnLocs);
            //spawnLocations.put(spawnLocations.size() + 1, Lists.newArrayList(new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
        } else {
            List<CoordLoc> spawnLocs;
            if (spawnLocations.containsKey(teamIndex)) {
                spawnLocs = spawnLocations.get(teamIndex);
                spawnLocs.add(new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            } else {
                spawnLocs = Lists.newArrayList(new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            }
            spawnLocations.put(teamIndex, spawnLocs);
        }
        addTeamCard(teamIndex);
    }

    private String getChatColor(int size) {
        double div14 = ((double) size + 1) / 14;
        long longDiv14 = (long) div14;
        double truncatedDiv14 = div14 - longDiv14;
        int remainderDiv14 = (int) (truncatedDiv14 * 14);
        switch (remainderDiv14) {
            case 1:
                return ChatColor.GREEN.toString();
            case 2:
                return ChatColor.RED.toString();
            case 3:
                return ChatColor.DARK_BLUE.toString();
            case 4:
                return ChatColor.YELLOW.toString();
            case 5:
                return ChatColor.WHITE.toString();
            case 6:
                return ChatColor.AQUA.toString();
            case 7:
                return ChatColor.GRAY.toString();
            case 8:
                return ChatColor.DARK_PURPLE.toString();
            case 9:
                return ChatColor.DARK_GREEN.toString();
            case 10:
                return ChatColor.BLUE.toString();
            case 11:
                return ChatColor.DARK_GRAY.toString();
            case 12:
                return ChatColor.BLACK.toString();
            case 13:
                return ChatColor.LIGHT_PURPLE.toString();
            case 14:
                return ChatColor.GOLD.toString();
            default:
                return ChatColor.GREEN.toString();
        }
    }

    private String getStringColor(int index) {
        /*double d = ((double) size + 1) / 14;
        long i = (long) d;
        double f = d - i;
        int s = (int) (f * 14);*/
        switch (index) {
            case 0:
                return "Lime";
            case 1:
                return "Red";
            case 2:
                return "Blue";
            case 3:
                return "Yellow";
            case 4:
                return "White";
            case 5:
                return "Cyan";
            case 6:
                return "Light Gray";
            case 7:
                return "Purple";
            case 8:
                return "Green";
            case 9:
                return "Light Blue";
            case 10:
                return "Gray";
            case 11:
                return "Black";
            case 12:
                return "Magenta";
            case 13:
                return "Orange";
            default:
                return "Lime";
        }
    }

    /**
     * Remove team slot by spawn location
     * @param loc Bukkit location of the spawn
     * @return
     *      Array of which index 0 is the index of the spawn removed in that team (if only 1 spawn location, team is completely removed.
     *      Index 1 is the index of the team removed from the map
     */
    public int[] removeTeamCard(Location loc) {
        CoordLoc locToRemove = new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (teamSize == 1 || !SkyWarsReloaded.getCfg().isUseSeparateCages()) {
            TeamCard toRemove = null;
            boolean removeWholeTeam = false;
            for (TeamCard tCard : teamCards) {
                if (tCard.getSpawns() != null && tCard.getSpawns().size() >= 1) {
                    for (int i = 0; i < tCard.getSpawns().size(); i++) {
                        if (tCard.getSpawns().get(i).equals(locToRemove)) {
                            toRemove = tCard;
                            tCard.getSpawns().remove(locToRemove);
                            if (tCard.getSpawns().size() == 0) {
                                removeWholeTeam = true;
                            }
                        }
                    }
                }
            }
            if (toRemove != null) {
                if (removeWholeTeam) {
                    teamCards.remove(toRemove);
                }
                for (int i : spawnLocations.keySet()) {
                    spawnLocations.get(i).remove(locToRemove);
                }
                saveArenaData();
                return new int[] { 0, toRemove.getPosition() };
            }
        } else {
            boolean foundMatch = false;
            List<TeamCard> cardsToRemove = new ArrayList<>();
            List<CoordLoc> spawnsOfTeam = new ArrayList<>();
            TeamCard tCardAffected = null;

            for (TeamCard tCard : teamCards) {
                if (tCard.getSpawns().contains(locToRemove)) {
                    spawnsOfTeam = spawnLocations.get(tCard.getPosition());
                    spawnsOfTeam.remove(locToRemove);
                    // If card empty -> remove completely
                    if (spawnsOfTeam.size() == 0) {
                        cardsToRemove.add(tCard);
                    }
                    //addTeamCard(tCard.getPosition() + 1, true); -- remove ??
                    foundMatch = true;
                    tCardAffected = tCard;
                    break;
                }
            }
            // Remove empty ones
            for (TeamCard tCard : cardsToRemove) {
                teamCards.remove(tCard);
            }
            // Save if modified
            if (foundMatch) {
                saveArenaData();
                // Use size since the removed spawn location for that team no longer exists, hence it's index was at maxIndex + 1 = size()
                return new int[] { spawnsOfTeam.size(), tCardAffected.getPosition() };
            }
        }
        return null;
    }

    public void addDeathMatchSpawn(Location loc) {
        addDeathMatchSpawn(new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        saveArenaData();
    }

    private void addDeathMatchSpawn(CoordLoc loc) {
        deathMatchSpawns.add(loc);
        saveArenaData();
    }

    public boolean removeDeathMatchSpawn(Location loc) {
        CoordLoc remove = new CoordLoc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        boolean result = deathMatchSpawns.remove(remove);
        saveArenaData();
        return result;
    }

    public void addChest(Chest chest, ChestPlacementType cpt) {
        ArrayList<CoordLoc> list;
        if (cpt == ChestPlacementType.NORMAL) {
            list = chests;
        } else {
            list = centerChests;
        }
        InventoryHolder ih = chest.getInventory().getHolder();
        if (ih instanceof DoubleChest) {
            DoubleChest dc = (DoubleChest) ih;
            Chest left = (Chest) dc.getLeftSide();
            Chest right = (Chest) dc.getRightSide();
            CoordLoc locLeft = new CoordLoc(left.getX(), left.getY(), left.getZ());
            CoordLoc locRight = new CoordLoc(right.getX(), right.getY(), right.getZ());
            if (!(list.contains(locLeft) || list.contains(locRight))) {
                addChest(locLeft, cpt, true);
            }
        } else {
            CoordLoc loc = new CoordLoc(chest.getX(), chest.getY(), chest.getZ());
            if (!list.contains(loc)) {
                addChest(loc, cpt, true);
            }
        }
    }

    private void addChest(CoordLoc loc, ChestPlacementType cpt, boolean save) {
        ArrayList<CoordLoc> list;
        if (cpt == ChestPlacementType.NORMAL) {
            list = chests;
        } else {
            list = centerChests;
        }
        list.add(loc);
        if (save) {
            saveArenaData();
        }
    }

    public void removeChest(Chest chest) {
        InventoryHolder ih = chest.getInventory().getHolder();
        if (ih instanceof DoubleChest) {
            DoubleChest dc = (DoubleChest) ih;
            Chest left = (Chest) dc.getLeftSide();
            Chest right = (Chest) dc.getRightSide();
            CoordLoc locLeft = new CoordLoc(left.getX(), left.getY(), left.getZ());
            CoordLoc locRight = new CoordLoc(right.getX(), right.getY(), right.getZ());
            chests.remove(locLeft);
            centerChests.remove(locLeft);
            chests.remove(locRight);
            centerChests.remove(locLeft);
            saveArenaData();
        } else {
            CoordLoc loc = new CoordLoc(chest.getX(), chest.getY(), chest.getZ());
            chests.remove(loc);
            centerChests.remove(loc);
            saveArenaData();
        }

    }

    public CoordLoc getSpectateSpawn() {
        return spectateSpawn;
    }

    public void setSpectateSpawn(Location location) {
        spectateSpawn = new CoordLoc(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        saveArenaData();
    }

    public void saveMap(Player mess) {
        boolean success = false;
        Location respawn = SkyWarsReloaded.getCfg().getSpawn();
        World editWorld = SkyWarsReloaded.get().getServer().getWorld(name);
        if (editWorld != null) {
            for (Player player : editWorld.getPlayers()) {
                player.teleport(respawn, TeleportCause.PLUGIN);
            }
            SkyWarsReloaded.getWM().unloadWorld(name, true);
            File dataDirectory = new File(SkyWarsReloaded.get().getDataFolder(), "maps");
            File target = new File(dataDirectory, name);
            SkyWarsReloaded.getWM().deleteWorld(target);
            File source = new File(SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath(), name);
            SkyWarsReloaded.getWM().copyWorld(source, target);
            if (mess != null) {
                mess.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", name).format("maps.saved"));
                mess.sendMessage(new Messaging.MessageFormatter().format("maps.register-reminder"));
            }
            SkyWarsReloaded.getWM().deleteWorld(source);
            saveArenaData();
            inEditing = false;
            success = true;
        }

        if (mess != null && !success) {
            mess.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", name).format("error.map-not-in-edit"));
        }
    }

    public ArrayList<MatchEvent> getEvents() {
        return events;
    }

    public ArrayList<CoordLoc> getDeathMatchSpawns() {
        return deathMatchSpawns;
    }

    public void removeDMSpawnBlocks() {
        for (CoordLoc loc : deathMatchSpawns) {
            World world = getCurrentWorld();
            Location loca = new Location(world, loc.getX(), loc.getY(), loc.getZ());
            world.getBlockAt(loca).setType(Material.AIR);
        }
    }

    public ArrayList<String> getDeathMatchWaiters() {
        return deathMatchWaiters;
    }

    public void addDeathMatchWaiter(Player player) {
        if (player != null) {
            deathMatchWaiters.add(player.getUniqueId().toString());
        }
    }

    public void clearDeathMatchWaiters() {
        deathMatchWaiters.clear();
    }

    public ArrayList<String> getAnvils() {
        return anvils;
    }

    public void addCrate(Location loc, int max) {
        crates.add(new Crate(loc, max));
    }

    public void removeCrates() {
        for (Crate crate : crates) {
            if (crate.getLocation() != null) {
                crate.getLocation().getWorld().getBlockAt(crate.getLocation()).setType(Material.AIR);
            }
        }
        crates.clear();
    }

    public ArrayList<Crate> getCrates() {
        return crates;
    }

    public Cage getCage() {
        return cage;
    }

    public void setCage(CageType next) {
        switch (next) {
            case CUBE:
                this.cage = new CubeCage();
                break;
            case DOME:
                this.cage = new DomeCage();
                break;
            case PYRAMID:
                this.cage = new PyramidCage();
                break;
            case SPHERE:
                this.cage = new SphereCage();
                break;
            case STANDARD:
                this.cage = new StandardCage();
                break;
            default:
                this.cage = new StandardCage();
        }
        saveArenaData();
    }

    public ArrayList<TeamCard> getTeamCards() {
        return teamCards;
    }

    GameQueue getJoinQueue() {
        return joinQueue;
    }

    public TeamCard getTeamCard(Player player) {
        for (TeamCard tCard : teamCards) {
            if (tCard.containsPlayer(player.getUniqueId()) != null) {
                return tCard;
            }
        }
        return null;
    }

    public int getNumTeamsOut() {
        int count = 0;
        for (TeamCard tCard : teamCards) {
            if (tCard.isElmininated()) {
                count++;
            }
        }
        return count;
    }

    public int getTeamsLeft() {
        return teamCards.size() - getNumTeamsOut();
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int size) {
        teamSize = size;
        for (TeamCard tCard : teamCards) {
            tCard.updateCard(size);
        }
        saveArenaData();
    }

    public TeamCard getWinningTeam() {
        for (TeamCard tCard : teamCards) {
            if (!tCard.isElmininated()) {
                return tCard;
            }
        }
        return null;
    }

    public int getFullTeams() {
        int count = 0;
        for (TeamCard tCard : teamCards) {
            if (tCard.isFull()) {
                count++;
            }
        }
        return count;
    }

    public String getCurrentChest() {
        return currentChest;
    }

    public void setCurrentChest(String voteString) {
        currentChest = voteString;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String voteString) {
        currentTime = voteString;
    }

    public String getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(String voteString) {
        currentHealth = voteString;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String voteString) {
        currentWeather = voteString;
    }

    public String getCurrentModifier() {
        return currentModifier;
    }

    public void setCurrentModifier(String voteString) {
        currentModifier = voteString;
    }

    public List<String> getWinners() {
        return winners;
    }

    public GameBoard getGameBoard() {
        return gameboard;
    }

    public TeamCard getTeamCardFromName(String name) {
        for (TeamCard tCard : teamCards) {
            if (tCard.getTeamName().equalsIgnoreCase(name)) {
                return tCard;
            }
        }
        return null;
    }

    public boolean allowScanvenger() {
        return allowScanvenger;
    }

    public boolean getProjectilesOnly() {
        return projectilesOnly;
    }

    public void setProjectilesOnly(boolean b) {
        projectilesOnly = b;
    }

    public boolean isProjectileSpleefEnabled() {
        return projectileSpleefEnabled;
    }

    public void setProjectileSpleefEnabled(boolean b) {
        projectileSpleefEnabled = b;
    }

    public boolean isDoubleDamageEnabled() {
        return doubleDamageEnabled;
    }

    public void setDoubleDamageEnabled(boolean b) {
        doubleDamageEnabled = b;
    }

    public boolean allowFriendlyFire() {
        return allowFriendlyFire;
    }

    public ChestPlacementType getChestPlacementType() {
        return chestPlacementType;
    }

    public void setChestPlacementType(ChestPlacementType chestPlacementType) {
        this.chestPlacementType = chestPlacementType;
    }

    public boolean isDisableDamage() {
        return disableDamage;
    }

    public void setDisableDamage(boolean b) {
        disableDamage = b;
    }

    public CoordLoc getWaitingLobbySpawn() {
        return waitingLobbySpawn;
    }

    public void setWaitingLobbySpawn(Location location) {
        waitingLobbySpawn = new CoordLoc(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        saveArenaData();
    }

    public void addWaitingPlayer(Player player) {
        waitingPlayers.add(player.getUniqueId());
    }

    public void removeWaitingPlayer(UUID uid) {
        waitingPlayers.remove(uid);
    }

    public void clearWaitingPlayers() {
        waitingPlayers = Lists.newArrayList();
    }

    public ArrayList<UUID> getWaitingPlayers() {
        return waitingPlayers;
    }

    public int getPlayerKills(Player p) {
        return playerKills.getOrDefault(p, 0);
    }

    public void increaseDisplayedKillsVar(Player p) {
        int newKills = getPlayerKills(p) + 1;
        playerKills.remove(p);
        playerKills.put(p, newKills);
    }

    public MatchEvent getNextEvent() {
        MatchEvent latest = null;
        int earliestStartTime = Integer.MAX_VALUE;
        for (MatchEvent event : getEvents()) {
            if (event.isEnabled() && event.getStartTime() > getTimer()) {
                if (event.getStartTime() < earliestStartTime) {
                    earliestStartTime = event.getStartTime();
                    latest = event;
                }
            }
        }

        return latest;
    }

    public static class TeamCardComparator implements Comparator<TeamCard> {
        @Override
        public int compare(TeamCard f1, TeamCard f2) {
            return Integer.compare(f1.getFullCount(), f2.getFullCount());
        }
    }

}
