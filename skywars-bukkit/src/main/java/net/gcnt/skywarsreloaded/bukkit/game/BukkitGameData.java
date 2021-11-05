package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.data.config.BukkitYAMLConfig;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.properties.MapDataProperties;
import net.gcnt.skywarsreloaded.game.GameData;
import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.ArrayList;
import java.util.List;

public class BukkitGameData implements GameData {

    private final BukkitSkyWarsReloaded plugin;
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

    public BukkitGameData(BukkitSkyWarsReloaded plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.config = new BukkitYAMLConfig(plugin, "gamedata-" + name, "maps", name + ".yml", "/mapdata.yml");
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
    public String getDisplayName() {
        return this.displayName;
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

    }

    @Override
    public synchronized void loadData() {
        this.displayName = config.getString(MapDataProperties.DISPLAY_NAME, name);
        this.creator = config.getString(MapDataProperties.CREATOR, "GCNT");
        this.teamSize = config.getInt(MapDataProperties.TEAM_SIZE, 1);
        this.minPlayers = config.getInt(MapDataProperties.MIN_PLAYERS, 4);

        this.lobbySpawn = Coord.fromString(config.getString(MapDataProperties.LOBBY_SPAWN, null));
        this.spectateSpawn = Coord.fromString(config.getString(MapDataProperties.SPECTATE_SPAWN, null));

        this.chests = new ArrayList<>();
        for (String chestLoc : config.getStringList(MapDataProperties.CHESTS)) {
            Coord loc = Coord.fromString(chestLoc);
            this.chests.add(loc);
        }
        this.signs = new ArrayList<>();
        for (String signLoc : config.getStringList(MapDataProperties.SIGNS)) {
            Coord loc = Coord.fromString(signLoc);
            this.signs.add(loc);
        }

        this.teamSpawnLocations = new ArrayList<>();
        try {
            List<List<String>> spawnPoints = (List<List<String>>) config.get(MapDataProperties.SPAWNPOINTS);
            spawnPoints.forEach(list -> {
                List<Coord> locs = new ArrayList<>();
                list.forEach(s -> locs.add(Coord.fromString(s)));
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
