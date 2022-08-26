package net.gcnt.skywarsreloaded.game;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.MapDataProperties;
import net.gcnt.skywarsreloaded.utils.results.SpawnAddResult;
import net.gcnt.skywarsreloaded.utils.results.SpawnRemoveResult;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class CoreGameTemplate implements GameTemplate {

    private final SkyWarsReloaded plugin;
    private final YAMLConfig config;
    private final String name;
    private final Object chestsLock;
    // Properties
    private String displayName;
    private String creator;
    // Player / Team data
    private SWCoord spectateSpawn;
    private SWCoord lobbySpawn;
    private int teamSize;
    private int minPlayers;
    private List<List<SWCoord>> teamSpawnLocations;
    // Map Data
    private int borderRadius;
    private Map<SWCoord, ChestType> chests;
    private List<SWChestTier> enabledChestTiers;
    private List<SWCoord> signs;
    private int maxPlayers;

    // State
    private boolean enabled;
    private boolean isTeamsizeSetup;

    public CoreGameTemplate(SkyWarsReloaded plugin, String name) {
        this.plugin = plugin;
        this.config = plugin.getYAMLManager().loadConfig("gamedata-" + name, FolderProperties.TEMPLATE_FOLDER.toString(), name + ".yml", "/mapdata.yml");
        this.name = name;
        this.chests = new HashMap<>();
        this.enabledChestTiers = new ArrayList<>();
        this.chestsLock = new Object();
        this.signs = new ArrayList<>();
        this.teamSpawnLocations = new ArrayList<>();
        this.displayName = name;
        this.creator = "GCNT";
        this.teamSize = 1;
        this.minPlayers = 4;
        this.maxPlayers = 0;
        this.borderRadius = 100;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCreator() {
        return this.creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public int getTeamSize() {
        return this.teamSize;
    }

    @Override
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public SWCoord getSpectateSpawn() {
        return this.spectateSpawn;
    }

    @Override
    public void setSpectateSpawn(SWCoord loc) {
        this.spectateSpawn = loc;
    }

    @Override
    public SWCoord getWaitingLobbySpawn() {
        return this.lobbySpawn;
    }

    @Override
    public void setWaitingLobbySpawn(SWCoord loc) {
        this.lobbySpawn = loc;
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public int getBorderRadius() {
        return borderRadius;
    }

    @Override
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    @Override
    public synchronized void loadData() {
        this.displayName = config.getString(MapDataProperties.DISPLAY_NAME.toString(), name);
        this.creator = config.getString(MapDataProperties.CREATOR.toString(), "GCNT");
        this.teamSize = config.getInt(MapDataProperties.TEAM_SIZE.toString(), 1);
        this.minPlayers = config.getInt(MapDataProperties.MIN_PLAYERS.toString(), 4);
        this.borderRadius = config.getInt(MapDataProperties.BORDER_RADIUS.toString(), 100);
        this.enabled = config.getBoolean(MapDataProperties.ENABLED.toString(), false);
        List<String> swChestTierIds = config.getStringList(MapDataProperties.ENABLED_CHESTTIERS.toString());
        List<SWChestTier> enabledChestTiersTmp = new ArrayList<>();
        for (String swChestTierId : swChestTierIds) {
            final SWChestTier chestType = this.plugin.getChestManager().getChestTierByName(swChestTierId);
            if (chestType == null) {
                plugin.getLogger().error("Invalid chest type found in the configuration of the template '" + name + "': " + swChestTierId + ". Ignoring it.");
            } else enabledChestTiersTmp.add(chestType);
        }
        this.enabledChestTiers = enabledChestTiersTmp;
        if (this.enabledChestTiers.isEmpty()) {
            this.enabledChestTiers.add(this.plugin.getChestManager().getChestTierByName("normal"));
        }

        this.isTeamsizeSetup = config.getBoolean(MapDataProperties.IS_TEAMSIZE_SETUP.toString(), false);

        String lspawn = config.getString(MapDataProperties.LOBBY_SPAWN.toString(), null);
        String sspawn = config.getString(MapDataProperties.SPECTATE_SPAWN.toString(), null);
        this.lobbySpawn = lspawn == null ? null : new CoreSWCoord(plugin, lspawn, false);
        this.spectateSpawn = sspawn == null ? null : new CoreSWCoord(plugin, sspawn, false);

        this.chests = new HashMap<>();
        final Set<String> chestKeys = config.getKeys(MapDataProperties.CHESTS.toString());
        if (chestKeys != null) {
            for (String chestTypeId : chestKeys) {
                ChestType chestType = ChestType.getById(chestTypeId);

                boolean valid = false;
                for (SWChestTier chestTier : enabledChestTiers) {
                    if (chestTier.hasChestType(chestType)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    plugin.getLogger().error("No chest type found in any of the map template's enabled chest tiers for the template '" + name + "': " + chestTypeId + ". Ignoring it.");
                } else {
                    final List<String> coordsList = config.getStringList(MapDataProperties.CHESTS + "." + chestTypeId);
                    for (String coordStr : coordsList) {
                        SWCoord loc = new CoreSWCoord(plugin, coordStr);
                        this.chests.put(loc, chestType);
                    }
                }
            }
        }
        this.signs = new ArrayList<>();
        for (String signLoc : config.getStringList(MapDataProperties.SIGNS.toString())) {
            SWCoord loc = new CoreSWCoord(plugin, signLoc);
            this.signs.add(loc);
        }

        this.teamSpawnLocations = new ArrayList<>();
        try {
            List<List<String>> spawnPoints = (List<List<String>>) config.get(MapDataProperties.SPAWNPOINTS.toString());
            if (spawnPoints != null) {
                spawnPoints.forEach(list -> {
                    if (list != null) {
                        List<SWCoord> locs = new ArrayList<>();
                        list.forEach(s -> locs.add(new CoreSWCoord(plugin, s)));
                        this.teamSpawnLocations.add(locs);
                    }
                });
            }
        } catch (Exception e) {
            plugin.getLogger().error("SkyWarsReloaded failed to load the spawnpoints of the game named '" + name + "'.");
            e.printStackTrace();
        }
        updateMaxPlayers();

        // todo load default voting options.
    }

    @Override
    public void saveData() {
        config.set(MapDataProperties.DISPLAY_NAME.toString(), name);
        config.set(MapDataProperties.CREATOR.toString(), creator);
        config.set(MapDataProperties.TEAM_SIZE.toString(), teamSize);
        config.set(MapDataProperties.MIN_PLAYERS.toString(), minPlayers);
        config.set(MapDataProperties.ENABLED.toString(), enabled);
        config.set(MapDataProperties.BORDER_RADIUS.toString(), borderRadius);

        System.out.println("enabledChestTypes is null ? " + (enabledChestTiers == null));
        for (SWChestTier enabledChestTier : enabledChestTiers) {
            System.out.println("enabledChestTier = " + enabledChestTier);
        }

        config.set(MapDataProperties.ENABLED_CHESTTIERS.toString(), enabledChestTiers.stream().map(SWChestTier::getName).collect(Collectors.toList()));

        config.set(MapDataProperties.IS_TEAMSIZE_SETUP.toString(), isTeamsizeSetup);

        config.set(MapDataProperties.LOBBY_SPAWN.toString(), lobbySpawn == null ? null : lobbySpawn.toString(false));
        config.set(MapDataProperties.SPECTATE_SPAWN.toString(), spectateSpawn == null ? null : spectateSpawn.toString(false));

        // Chests
        ChestType defaultChest = ChestType.ISLAND;
        // Make sure chest types are valid before storing the data
        chests.replaceAll((swCoord, swChestType) -> swChestType == null ? defaultChest : swChestType);
        // Store chest coords under each respective type
        final Map<String, List<String>> chestCoordsByType = new HashMap<>();
        this.chests.forEach((coord, type) -> {
            String chestName = type.getId();
            List<String> coords = chestCoordsByType.getOrDefault(chestName, new ArrayList<>());
            coords.add(coord.toString(false));
            chestCoordsByType.put(chestName, coords);
        });

        config.set(MapDataProperties.CHESTS.toString(), chestCoordsByType);
        config.set(MapDataProperties.SIGNS.toString(), signs.stream().map(SWCoord::toString).collect(Collectors.toList()));

        List<List<String>> spawnPoints = new ArrayList<>();
        for (List<SWCoord> list : this.teamSpawnLocations) {
            spawnPoints.add(list.stream().map((coord) -> coord.toString(false)).collect(Collectors.toList()));
        }
        config.set(MapDataProperties.SPAWNPOINTS.toString(), spawnPoints);
        config.save();
    }

    @Override
    public List<SWChestTier> getEnabledChestTiers() {
        return this.enabledChestTiers;
    }

    @Override
    public void enableChestType(SWChestTier chestType) {
        if (!this.enabledChestTiers.contains(chestType)) this.enabledChestTiers.add(chestType);
    }

    @Override
    public boolean addChest(SWCoord loc, ChestType chestType) {
        synchronized (this.chestsLock) {
            for (SWCoord chest : this.chests.keySet()) {
                if (chest.equals(loc)) return false;
            }
            this.chests.put(loc, chestType);
        }
        return true;
    }

    @Override
    public synchronized boolean removeChest(SWCoord loc) {
        for (SWCoord chest : this.chests.keySet()) {
            if (chest.equals(loc)) {
                this.chests.remove(chest);
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<SWCoord, ChestType> getChests() {
        return this.chests;
    }

    @Override
    public List<List<SWCoord>> getTeamSpawnpoints() {
        return this.teamSpawnLocations;
    }

    @Override
    public synchronized SpawnAddResult addSpawn(int team, SWCoord loc) {
        // player entered invalid index.
        if (team < 0 && teamSize != 1) {
            return SpawnAddResult.INDEX_TOO_LOW;
        }
        if (team > teamSpawnLocations.size() && teamSize != 1) {
            return SpawnAddResult.INDEX_TOO_HIGH;
        }

        // checking if there is already a team with this spawn location. #stupidproof
        for (List<SWCoord> locs : teamSpawnLocations) {
            for (SWCoord loc1 : locs) {
                if (loc1.equals(loc)) return SpawnAddResult.SPAWN_ALREADY_EXISTS;
            }
        }

        // checking if the teamsize is 1, or if they entered the current size (team + 1)
        if (teamSize == 1 || team == teamSpawnLocations.size()) {
            // add new team to the list.
            this.teamSpawnLocations.add(Lists.newArrayList(loc));
            return SpawnAddResult.NEW_TEAM_ADDED;
        }

        if (teamSize > 1) {
            List<SWCoord> coords = this.teamSpawnLocations.get(team);
            if (coords.size() >= getTeamSize()) {
                return SpawnAddResult.MAX_TEAM_SPAWNS_REACHED;
            }
        }

        // adding spawn to team.
        this.teamSpawnLocations.get(team).add(loc);
        updateMaxPlayers();
        return SpawnAddResult.TEAM_UPDATED;
    }

    @Override
    public synchronized SpawnRemoveResult removeSpawn(SWCoord loc) {
        for (int team = 0; team < getTeamSpawnpoints().size(); team++) {
            final List<SWCoord> swCoords = getTeamSpawnpoints().get(team);
            for (int pos = 0; pos < swCoords.size(); pos++) {
                if (swCoords.get(pos).equals(loc.asBlock())) {
                    swCoords.remove(pos);
                    int remaining = swCoords.size();
                    if (swCoords.isEmpty()) teamSpawnLocations.remove(team);
                    return new SpawnRemoveResult(true, team, pos, remaining);
                }
            }
        }
        updateMaxPlayers();
        return new SpawnRemoveResult(false, 0, 0, 0);
    }

    @Override
    public YAMLConfig getConfig() {
        return this.config;
    }

    @Override
    public void setIsTeamSizeSetup(boolean setup) {
        this.isTeamsizeSetup = setup;
    }

    @Override
    public boolean isTeamSizeSetup() {
        return this.isTeamsizeSetup;
    }

    @Override
    public boolean checkToDoList(SWCommandSender player) {
        if (!isTeamsizeSetup) {
            player.sendMessage(plugin.getUtils().colorize("&a&lLet's start with the teamsize! &7Use &e/swm teamsize <size> &7to change the team size of your game. Use 2 for Duos, 3 for Trios etc."));
            return false;
        }
        if (teamSize > 1 && lobbySpawn == null) { // todo add a check, probs from config, if they enabled the waiting lobby for single players too.
            player.sendMessage(plugin.getUtils().colorize("&a&lNow let's set the lobby spawn! &7This is the location that we send players to when they join the game, before the cages. Use &e/swm spawn lobby &7 to set it."));
            return false;
        }

        if (spectateSpawn == null) {
            player.sendMessage(plugin.getUtils().colorize("&a&lLet's move on to the spectator spawn! &7This is the location that we send players to when they die or spectate the game. Use &e/swm spawn spectate&7 to set it."));
            return false;
        }

        if (chests.isEmpty()) {
            player.sendMessage(plugin.getUtils().colorize("&a&lLet's add our first chest! &7Go ahead and place your first chest."));
            return false;
        }

        int found = 0;
        for (int i = 0; i < teamSpawnLocations.size(); i++) {
            List<SWCoord> coords = teamSpawnLocations.get(i);
            int teamNumber = i + 1;
            if (coords.size() < teamSize) {
                int spawnsLeft = teamSize - coords.size();
                player.sendMessage(plugin.getUtils().colorize(String.format("&a&lLet's set %d more spawn(s) for team %d! &7Use &e/swm spawn player %d &7to add a spawn.", spawnsLeft, teamNumber, teamNumber)));
                return false;
            } else if (coords.size() > teamSize) {
                int spawnsLeft = coords.size() - teamSize;
                player.sendMessage(plugin.getUtils().colorize(String.format("&c&lTeam %d has too many spawns! &7Remove %d spawn(s) from this team by breaking one of its beacons.", teamNumber, spawnsLeft)));
                return false;
            }
            found += coords.size();
        }

        if (found == 0) {
            StringBuilder sb = new StringBuilder("&a&lLet's add our first player spawn! &7Use /swm spawn player ");
            if (teamSize > 1) sb.append("1 ");
            sb.append("&7to set a player spawn");
            if (teamSize > 1) sb.append(" where 1 is the team number");
            sb.append(".");
            player.sendMessage(plugin.getUtils().colorize(sb.toString()));
        }

        return true;
    }

    private void updateMaxPlayers() {
        this.maxPlayers = this.getTeamSpawnpoints().stream().mapToInt(List::size).sum();
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Override
    public boolean isAllowedDispersedParties() {
        Object obj = config.get(MapDataProperties.ALLOW_DISPERSED_PARTIES.toString(), null);
        if (obj instanceof Boolean) return (boolean) obj;
        return this.plugin.getConfig().getBoolean(ConfigProperties.PARTIES_ALLOW_DISPERSED_PARTIES.toString());
    }
}
