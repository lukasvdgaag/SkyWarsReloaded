package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public class TeamSpawn {

    private final Team team;
    private final Coord location;
    private SWPlayer player;

    public TeamSpawn(Team team, Coord location) {
        this.team = team;
        this.location = location;
        this.player = null;
    }

    public boolean isOccupied() {
        return this.player != null;
    }

    public Coord getLocation() {
        return location;
    }

    /*
    Get methods
     */

    public SWPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SWPlayer player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }
}
