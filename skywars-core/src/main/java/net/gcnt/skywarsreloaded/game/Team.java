package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final GameMap gameMap;
    private final int teamNumber;
    private final List<TeamSpawn> spawns;

    public Team(GameMap gameMap, int number, List<Coord> spawnLocations) {
        this.gameMap = gameMap;
        this.teamNumber = number;

        this.spawns = new ArrayList<>();
        spawnLocations.forEach(coord -> spawns.add(new TeamSpawn(this, coord)));
    }

    public boolean canAddPlayer() {
        for (TeamSpawn spawn : spawns) {
            if (!spawn.isOccupied()) return true;
        }
        return false;
    }

    public boolean addPlayer(SWPlayer player) {
        if (!canAddPlayer()) return false;
        for (TeamSpawn spawn : spawns) {
            if (!spawn.isOccupied()) {
                spawn.setPlayer(player);
                return true;
            }
        }
        return false;
    }

    public boolean removePlayer(SWPlayer player) {
        for (TeamSpawn spawn : spawns) {
            if (spawn.getPlayer() != null && spawn.getPlayer().equals(player)) {
                spawn.setPlayer(null);
                return true;
            }
        }
        return false;
    }

    /*
    Get methods
     */

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public List<TeamSpawn> getSpawns() {
        return spawns;
    }
}
