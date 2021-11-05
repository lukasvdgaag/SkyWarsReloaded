package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public class TeamSpawn implements Spawn {

    private final Team team;
    private final Coord location;
    private List<GamePlayer> players;

    public TeamSpawn(Team team, Coord location) {
        this.team = team;
        this.location = location;
        this.players = null;
    }

    @Override
    public boolean isOccupied() {
        return players.size() < this.team.getGameWorld().getGame().getTeamSize();
    }

    @Override
    public Coord getLocation() {
        return location;
    }

    /*
    Get methods
     */

    @Override
    public List<GamePlayer> getPlayers() {
        return players;
    }

    @Override
    public void addPlayer(GamePlayer player) {

    }

    @Override
    public void removePlayer(GamePlayer player) {

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
