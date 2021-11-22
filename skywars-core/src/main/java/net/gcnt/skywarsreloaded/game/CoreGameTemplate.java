package net.gcnt.skywarsreloaded.game;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.MapDataProperties;
import net.gcnt.skywarsreloaded.utils.results.SpawnAddResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoreGameTemplate implements GameTemplate {

    private final SkyWarsReloaded plugin;
    private final YAMLConfig config;
    private final String name;
    private String displayName;
    private String creator;
    private SWCoord spectateSpawn;
    private SWCoord lobbySpawn;
    private int teamSize;
    private int minPlayers;
    private List<SWCoord> chests;
    private List<SWCoord> signs;
    private List<List<SWCoord>> teamSpawnLocations;
    private int borderRadius;
    private boolean enabled;

    public CoreGameTemplate(SkyWarsReloaded plugin, String name) {
        this.plugin = plugin;
        this.config = plugin.getYAMLManager().loadConfig("gamedata-" + name, FolderProperties.TEMPLATE_FOLDER.toString(), name + ".yml", "/mapdata.yml");
        this.name = name;
        this.chests = new ArrayList<>();
        this.signs = new ArrayList<>();
        this.teamSpawnLocations = new ArrayList<>();
        this.displayName = name;
        this.creator = "GCNT";
        this.spectateSpawn = null;
        this.lobbySpawn = null;
        this.teamSize = 1;
        this.minPlayers = 4;
        this.borderRadius = 100;
        this.enabled = false;
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

        String lspawn = config.getString(MapDataProperties.LOBBY_SPAWN.toString(), null);
        String sspawn = config.getString(MapDataProperties.SPECTATE_SPAWN.toString(), null);
        this.lobbySpawn = lspawn == null ? null : new CoreSWCoord(plugin, lspawn);
        this.spectateSpawn = sspawn == null ? null : new CoreSWCoord(plugin, sspawn);

        this.chests = new ArrayList<>();
        for (String chestLoc : config.getStringList(MapDataProperties.CHESTS.toString())) {
            SWCoord loc = new CoreSWCoord(plugin, chestLoc);
            this.chests.add(loc);
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

        config.set(MapDataProperties.LOBBY_SPAWN.toString(), lobbySpawn == null ? null : lobbySpawn.toString());
        config.set(MapDataProperties.SPECTATE_SPAWN.toString(), spectateSpawn == null ? null : spectateSpawn.toString());

        config.set(MapDataProperties.CHESTS.toString(), chests.stream().map(SWCoord::toString).collect(Collectors.toList()));
        config.set(MapDataProperties.SIGNS.toString(), signs.stream().map(SWCoord::toString).collect(Collectors.toList()));

        List<List<String>> spawnPoints = new ArrayList<>();
        for (List<SWCoord> list : this.teamSpawnLocations) {
            spawnPoints.add(list.stream().map(SWCoord::toString).collect(Collectors.toList()));
        }
        config.set(MapDataProperties.SPAWNPOINTS.toString(), spawnPoints);
        config.save();
    }

    @Override
    public synchronized boolean addChest(SWCoord loc) {
        for (SWCoord chest : this.chests) {
            if (chest.equals(loc)) return false;
        }
        this.chests.add(loc);
        return true;
    }

    @Override
    public synchronized boolean removeChest(SWCoord loc) {
        for (SWCoord chest : this.chests) {
            if (chest.equals(loc)) {
                this.chests.remove(loc);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<SWCoord> getChests() {
        return this.chests;
    }

    @Override
    public List<List<SWCoord>> getTeamSpawnpoints() {
        return this.teamSpawnLocations;
    }

    @Override
    public SpawnAddResult addSpawn(int team, SWCoord loc) {
        // player entered invalid index.
        if (team < 0) {
            return SpawnAddResult.INDEX_TOO_LOW;
        }
        if (team > teamSpawnLocations.size()) {
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

        // adding spawn to team.
        this.teamSpawnLocations.get(team).add(loc);
        return SpawnAddResult.TEAM_UPDATED;
    }

    @Override
    public void removeSpawn(SWCoord loc) {
        for (List<SWCoord> locs : getTeamSpawnpoints()) {
            locs.removeIf(coord -> coord.equals(loc));
        }
    }
}
