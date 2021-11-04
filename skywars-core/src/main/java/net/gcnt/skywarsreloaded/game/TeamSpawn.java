package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.List;

public class TeamSpawn implements Spawn {

    private final Team team;
    private final Coord location;
    private List<SWPlayer> players;

    public TeamSpawn(Team team, Coord location) {
        this.team = team;
        this.location = location;
        this.players = null;
    }

    @Override
    public boolean isOccupied() {
        return players.size() < this.team.getArena().getTeamSize();
    }

    @Override
    public Coord getLocation() {
        return location;
    }

    /*
    Get methods
     */

    @Override
    public List<SWPlayer> getPlayers() {
        return null;
    }

    @Override
    public void addPlayer(SWPlayer player) {

    }

    @Override
    public void removePlayer(SWPlayer player) {

    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public void determineCageDesign() {

    }

    @Override
    public void updateCage() {

    }
}
