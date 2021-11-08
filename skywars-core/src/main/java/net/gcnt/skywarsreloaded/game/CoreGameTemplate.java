package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.utils.Coordinate;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.MapDataProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoreGameTemplate implements GameTemplate {

    private final SkyWarsReloaded plugin;
    private final YAMLConfig config;
    private final String name;
    private String displayName;
    private String creator;
    private Coord spectateSpawn;
    private Coord lobbySpawn;
    private int teamSize;
    private int minPlayers;
    private List<Coord> chests;
    private List<Coord> signs;
    private List<List<Coord>> teamSpawnLocations;
    private boolean enabled;

    public CoreGameTemplate(SkyWarsReloaded plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.config = plugin.getYAMLManager().loadConfig("gamedata-" + name, FolderProperties.TEMPLATE_FOLDER.toString(), name + ".yml", "/mapdata.yml");
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
    public Coord getSpectateSpawn() {
        return this.spectateSpawn;
    }

    @Override
    public void setSpectateSpawn(Coord loc) {
        this.spectateSpawn = loc;
    }

    @Override
    public Coord getWaitingLobbySpawn() {
        return this.lobbySpawn;
    }

    @Override
    public void setWaitingLobbySpawn(Coord loc) {
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
    public synchronized void loadData() {
        this.displayName = config.getString(MapDataProperties.DISPLAY_NAME.toString(), name);
        this.creator = config.getString(MapDataProperties.CREATOR.toString(), "GCNT");
        this.teamSize = config.getInt(MapDataProperties.TEAM_SIZE.toString(), 1);
        this.minPlayers = config.getInt(MapDataProperties.MIN_PLAYERS.toString(), 4);
        this.enabled = config.getBoolean(MapDataProperties.ENABLED.toString(), false);

        this.lobbySpawn = new Coordinate(plugin, config.getString(MapDataProperties.LOBBY_SPAWN.toString(), null));
        this.spectateSpawn = new Coordinate(plugin, config.getString(MapDataProperties.SPECTATE_SPAWN.toString(), null));

        this.chests = new ArrayList<>();
        for (String chestLoc : config.getStringList(MapDataProperties.CHESTS.toString())) {
            Coord loc = new Coordinate(plugin, chestLoc);
            this.chests.add(loc);
        }
        this.signs = new ArrayList<>();
        for (String signLoc : config.getStringList(MapDataProperties.SIGNS.toString())) {
            Coord loc = new Coordinate(plugin, signLoc);
            this.signs.add(loc);
        }

        this.teamSpawnLocations = new ArrayList<>();
        try {
            List<List<String>> spawnPoints = (List<List<String>>) config.get(MapDataProperties.SPAWNPOINTS.toString());
            spawnPoints.forEach(list -> {
                List<Coord> locs = new ArrayList<>();
                list.forEach(s -> locs.add(new Coordinate(plugin, s)));
                this.teamSpawnLocations.add(locs);
            });
        } catch (Exception e) {
            plugin.getLogger().severe("SkyWarsReloaded failed to load the spawnpoints of the game named '" + name + "'.");
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

        config.set(MapDataProperties.LOBBY_SPAWN.toString(), lobbySpawn.toString());
        config.set(MapDataProperties.SPECTATE_SPAWN.toString(), spectateSpawn.toString());

        config.set(MapDataProperties.CHESTS.toString(), chests.stream().map(Coord::toString).collect(Collectors.toList()));
        config.set(MapDataProperties.SIGNS.toString(), signs.stream().map(Coord::toString).collect(Collectors.toList()));

        List<List<String>> spawnPoints = new ArrayList<>();
        for (List<Coord> list : this.teamSpawnLocations) {
            spawnPoints.add(list.stream().map(Coord::toString).collect(Collectors.toList()));
        }
        config.set(MapDataProperties.SPAWNPOINTS.toString(), spawnPoints);
        config.save();
    }

    @Override
    public synchronized boolean addChest(Coord loc) {
        for (Coord chest : this.chests) {
            if (chest.equals(loc)) return false;
        }
        this.chests.add(loc);
        return true;
    }

    @Override
    public synchronized boolean removeChest(Coord loc) {
        for (Coord chest : this.chests) {
            if (chest.equals(loc)) {
                this.chests.remove(loc);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Coord> getChests() {
        return this.chests;
    }

    @Override
    public List<List<Coord>> getTeamSpawnpoints() {
        return this.teamSpawnLocations;
    }

    @Override
    public void addSpawn(int team, Coord loc) {
        // todo add the spawn. Didn't have the logic.
    }

    @Override
    public void removeSpawn(Coord loc) {
        for (List<Coord> locs : getTeamSpawnpoints()) {
            locs.removeIf(coord -> coord.equals(loc));
        }
    }
}
