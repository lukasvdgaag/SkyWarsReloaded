package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class CoreTeamSpawn implements TeamSpawn {

    private final GameTeam team;
    private final Coordinate location;
    private TeamCage cage;
    private List<GamePlayer> players;
    private String cageDesign;

    public CoreTeamSpawn(GameTeam team, Coordinate location) {
        this.team = team;
        this.location = location;
        this.players = new ArrayList<>();
        this.cageDesign = "default";
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
        if (isOccupied()) return;

        players.add(player);
        determineCageDesign();
        // todo update cage here?
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

    @Override
    public void removeCage() {
        if (cage.isPlaced()) return;
        cage.removeCage(this.cageDesign);
    }

    @Override
    public TeamCage getCage() {
        return this.cage;
    }

    @Override
    public void setCage(TeamCage cage) {
        this.cage = cage;
        // todo update cage here?
    }
}
