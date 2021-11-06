package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coordinate;

import java.util.List;

public class CoreTeamSpawn implements TeamSpawn {

    private final GameTeam team;
    private final Coordinate location;
    private List<GamePlayer> players;

    public CoreTeamSpawn(GameTeam team, Coordinate location) {
        this.team = team;
        this.location = location;
        this.players = null;
    }

    @Override
    public boolean isOccupied() {
        return players.size() < this.team.getGameWorld().getGame().getTeamSize();
    }

    @Override
    public Coordinate getLocation() {
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
    public GameTeam getTeam() {
        return team;
    }

    @Override
    public void determineCageDesign() {

    }

    @Override
    public void updateCage() {

    }
}
